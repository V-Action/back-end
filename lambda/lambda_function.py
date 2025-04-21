import boto3
import pandas as pd
import io

# Cliente S3
s3 = boto3.client('s3')

def lambda_handler(event, context):
    # Obtém informações do evento
    record = event['Records'][0]
    bucket_raw = record['s3']['bucket']['name']
    file_key = record['s3']['object']['key']
    
    # Nome do bucket de destino
    bucket_trusted = "trusted-bucket-vaction"

    try:
        # Baixa o arquivo do S3
        response = s3.get_object(Bucket=bucket_raw, Key=file_key)
        file_content = response['Body'].read()

        # Lê o CSV com Pandas
        df = pd.read_csv(io.BytesIO(file_content))

        ''' 🔹 Tratamento de dados '''
        # limpa os valores nulos do dataframe
        df_nan = df.dropna()
        # garante que a Data_Solicitacao seja um datetime
        df_nan['Data_Solicitacao'] = pd.to_datetime(df_nan['Data_Solicitacao'], format='%d/%m/%Y')
        # criando uma coluna apenas com o ano
        df_nan['Ano_Solicitacao'] = df_nan['Data_Solicitacao'].dt.year
        # criando um subset com as prioridades
        df_prioridade = df_nan[(df_nan['Ano_Solicitacao'] > 2023) & (df_nan['Observacoes'] == 'Urgente')]

        # Convertendo o DataFrame para CSV
        output_buffer = io.BytesIO()
        df_prioridade.to_csv(output_buffer, index=False)
        output_buffer.seek(0)

        # Salvando no bucket trusted
        s3.put_object(Bucket=bucket_trusted, Key=file_key, Body=output_buffer.getvalue())

        print(f"Arquivo {file_key} processado e salvo como {file_key} no {bucket_trusted}")
    
    except Exception as e:
        print(f"Erro ao processar o arquivo {file_key}: {str(e)}")