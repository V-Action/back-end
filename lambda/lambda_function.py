import boto3
import pandas as pd
import io

# SNS
sns_client = boto3.client('sns')

# Cliente S3
s3 = boto3.client('s3')

def lambda_handler(event, context):
    # Obtém informações do evento
    record = event['Records'][0]
    bucket_raw = record['s3']['bucket']['name']
    file_key = record['s3']['object']['key']
    
    # Nome do bucket de destino
    bucket_trusted = "trusted-bucket-vaction"

    ''' Envio de mensagem personalizada ao SNS '''
    resultado = {
        "status": "Sucesso",
        "usuario": "gabriel.bduarte@sptech.school",
        "acao": "processamento de dados"
    }

    mensagem_formatada_sucesso = f"""
    Olá! A função Lambda foi executada com sucesso no ETL.
    Detalhes:
    - Status: {resultado['status']}
    - Usuário: {resultado['usuario']}
    - Ação executada: {resultado['acao']}
    Att, seu sistema automatizado
    """

    mensagem_formatada_erro = f"""
    Olá! A função Lambda foi executada com falha no ETL.
    Detalhes:
    - Status: {resultado['status']}
    - Usuário: {resultado['usuario']}
    - Ação executada: {resultado['acao']}
    Att, seu sistema automatizado
    """

    try:
        # Baixa o arquivo do S3
        s3_response = s3.get_object(Bucket=bucket_raw, Key=file_key)
        file_content = s3_response['Body'].read()

        # Lê o CSV com Pandas
        df = pd.read_csv(io.BytesIO(file_content))

        ''' 🔹 Tratamento de dados '''
        df_nan = df.dropna()
        df_nan['Data_Solicitacao'] = pd.to_datetime(df_nan['Data_Solicitacao'], format='%d/%m/%Y')
        df_nan['Ano_Solicitacao'] = df_nan['Data_Solicitacao'].dt.year
        df_prioridade = df_nan[
            (df_nan['Ano_Solicitacao'] > 2023) & (df_nan['Observacoes'] == 'Urgente')
        ]

        # Convertendo para CSV
        output_buffer = io.BytesIO()
        df_prioridade.to_csv(output_buffer, index=False)
        output_buffer.seek(0)

        # Salvando no bucket de destino
        s3.put_object(Bucket=bucket_trusted, Key=file_key, Body=output_buffer.getvalue())

        print(f"Arquivo {file_key} processado e salvo como {file_key} no {bucket_trusted}")

        sns_client.publish(
            TopicArn='arn:aws:sns:us-east-1:359195580579:email-vaction',
            Message=mensagem_formatada_sucesso,
            Subject='[Notificação Lambda] Sucesso na execução'
        )

    except Exception as e:
        print(f"Erro ao processar o arquivo {file_key}: {str(e)}")

        sns_client.publish(
            TopicArn='arn:aws:sns:us-east-1:359195580579:email-vaction',
            Message=mensagem_formatada_erro,
            Subject='[Notificação Lambda] Falha na execução'
        )
