{{/* Define a helper to generate the full MySQL connection URL */}}
{{- define "spring-app.MySQLConnectionURL" -}}
jdbc:mysql://{{ .Release.Name }}-mysql-service:3307/{{ .Values.mysql.databaseName }}
{{- end -}}
