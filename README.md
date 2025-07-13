# Finden

## Vorbereitungen / Setup

Initiales Setup für die lokale Entwicklung.

### Gitlab API Token

Damit npm auf die Repositories in Gitlab zugreifen kann, muss in Gitlab ein Personal Access Token erstellt werden und
lokal in der `~/.zshrc` hinterlegt werden:
`export CI_JOB_TOKEN=<token>`

### Aiven Schema Registry

Für einen erfolgreichen Build müssen die in anderen Services definierten Avro Schemas auf der lokalen Maschine
vorliegen. Hierzu ist vor dem Kompilieren der Gradle Jobs `downloadSchemas` auszuführen, der die Schemas aus der Schema
Registry herunterlädt. Damit der Job erfolgreich beendet werden kann, sind zuvor noch drei Umgebungsvariablen
zu definieren (beispielweise in der `~/.bashrc` oder `~/.zshrc`):

```bash
export AIVEN_SCHEMA_REGISTRY_USER=erkunden-finden-backend-produkte-consumer
export AIVEN_SCHEMA_REGISTRY_URL=https://entscheiden-kafka-cluster-entscheiden-dev-project.aivencloud.com:20609
export AIVEN_SCHEMA_REGISTRY_PASSWORD=<PASSWORD>
```

```bash
# Starten des Gradle Tasks via CLI
./gradlew downloadSchemas
```


Damit Gradle diese neuen Umgebungsvariablen bekannt sind, muss die editierte Datei möglicherweise mithilfe des `source`
-Befehels neu eingelesen werden:

```bash
source ~/.bashrc
# bzw.:
source ~/.zshrc
```

Anschließend können die Schemas mit folgendem Befehl heruntergeladen werden:

```bash
./gradlew downloadSchemas
```

### Env

Für die lokale Entwicklung müssen die folgenden Dateien kopiert werden:
* `.env.example` -> `.env`
* `.env.fastify.example` -> `.env.fastify`
* `frontend/.env.example` -> `frontend/.env`

Diese Dateien werden nur für die lokale Entwicklung bzw. Test-Ausführung benötigt.
Für das Deployment werden Gitlab-CI Variablen verwendet oder die Default-Werte.

### Befehle

Bevor entwickelt wird, müssen folgende Befehle ausgeführt werden:

```bash
# Abhängigkeiten installieren
npm ci

# Bei Änderung an den Dependencies bitte full install, weil dadurch die package-lock.json angepasst wird
npm install

# git hooks installieren, damit die Linter beim Commiten ausgeführt werden
husky install
```

### Intellij einrichten

#### Prettier einrichten

siehe https://dokumentation.ecom.blume2000.de/ecomit/guides/dev-tools.html#prettier

#### ESLint konfigurieren

Unter "Languages & Frameworks -> Javascript -> Code Quality Tools -> ESLint" den Haken bei "Automatic ESLint
configuration" setzen.

## Startup

### Backend starten

Docker:

```bash
# Container starten
docker-compose -f docker/docker-compose.yaml up -d

# Container stoppen
docker-compose -f docker/docker-compose.yaml stop

# Container entfernen
docker-compose -f docker/docker-compose.yaml down
```

Anwendung:

```bash
./gradlew quarkusDev
```

Das Backend ist anschließend unter http://localhost:8081/ verfügbar.

### Frontend starten

```bash
# Abhängigkeiten installieren
npm ci

# Anwedung starten
npm run dev
```

Das Frontend ist anschließend unter http://localhost:8082/ verfügbar.

### Frontend mit Server-Side-Rendering (SSR) starten

Dazu muss der [Frontend-Proxy](https://gitlab.com/blume2000/ecom/shared/frontend-proxy#lokales-debugging) lokal
gestartet werden. Nachdem die Anleitung befolgt und der Frontend-Proxy gestartet wurde, ist dieser dann
unter http://localhost:8080/ verfügbar. Mit folgenden Befehlen wird die Anwendung gestartet:

```bash
# Abhängigkeiten installieren
npm ci

# Anwendung bauen
npm run build

# Anwedung starten
npm run server
```

Anschließend ist die Anwendung über den Frontend-Proxy erreichbar und wird mit SSR ausgeliefert.

#### SSR ohne Frontend-Proxy

Man kann das SSR auch ohne den Frontend-Proxy ausliefern, indem man in `frontend/.env` den Port der BASE-API-URL für das
Backend ändert:
`VUE_APP_PRODUKTE_API_HOST=http://localhost:8081`
Damit man aber ein realistisches Verhalten testen kann (z.B. Basic Auth), sollte der Frontend-Proxy verwendet werden.

#### Wie sieht ein Crawler die Seite?

Um zu testen, wie die Anwendung ohne Client/Javascript aussieht, muss jetzt nur noch im Browser Javascript deakiviert
werden.

## Linter ausführen

```bash
# Backend
./gradlew detekt

# Frontend
npm run lint
```

## Tests  ausführen

```bash
# Backend
./gradlew test

# Frontend
npm run test

# Snapshots aktualisieren
npm run unitTest — -u
```

## importing stackdriver dashboard

```
# execute from repository root
# find id in the list below (the last part of the name field)
gcloud monitoring dashboards list --project=b2k-ec-shared-operations-83cf
gcloud monitoring dashboards describe <YOUR_ID> --format=json --project=b2k-ec-shared-operations-83cf > <DASHBOARD_NAME>.json
mv DASHBOARD_NAME infrastructure/operations/
```

## Slack Notification Channels einrichten

1. Alerting
   Dashboard [öffnen](https://console.cloud.google.com/monitoring/alerting?authuser=1&project=b2k-ec-shared-operations-83cf)
2. Edit Notification channels
3. Slack: Add new

## Uptime Check einrichten

1. Uptime Checkes
   Dashboard [öffnen](https://console.cloud.google.com/monitoring/uptime?authuser=1&project=b2k-ec-shared-operations-83cf)
2. Create Uptime Check
3. Folgende Felder müssen ausgefüllt werden:

- Titel: "erkunden finden Dev"
- Protocol: HTTPS
- Resource Type: URL
- Hostname: dev.ecom.blume2000.de
- Path: /
- Check Frequency: 1 minute
- Regions: Global
- Port: 443
- Username: *confidential*
- Password: *confidential*
- Response Timeout: 10 seconds
- Alert Name: erkunden finden Dev uptime failure
- Duration: 1 minute
- Notification Channel: Erkunden Alerting

4. Speichern

## Slack Uptime Alert einrichten

Für die Einrichtung eines Uptime Checks wird die Terraform Resource `google_monitoring_uptime_check_config`
verwendet. Für eine Downtime-Benachrichtigung in einem Slack Channel sind folgende manuelle Schritte notwendig:

1. GCP Account mit folgenden Rechten ausstatten:

- monitoring.uptimeCheckConfigEditor
- monitoring.notificationChannelEditor
- monitoring.alertPolicyEditor

1. Uptime Checks
   Dashboard [öffnen](https://console.cloud.google.com/monitoring/uptime?authuser=1&project=b2k-ec-shared-operations-83cf)
2. Gewünschten Uptime Check bearbeiten
3. Unter Alert & Notification die Alert Condition aktivieren und den passenden Notification Channel auswählen
4. Änderungen speichern

## Domain Mapping

Der Service Account für den Service muss manuell als Inhaber in der Webmasterskonsole eintragen
werden: https://www.google.com/webmasters/verification/details?hl=de&domain=ecom.blume2000.de

Es können nur berechtigte Personen die Webmasterkonsole nutzen.

## Aiven

Für die Authentifizierung bei Aiven werden Zertifikate verwendet. Um diese Zertifikate zu erzeugen, müssen ein truststore
und ein keystore erzeugt werden. Dies geht wie folgt :

```
openssl pkcs12 -export -inkey service.key -in service.cert -out client.keystore.p12 -name service_key
keytool -import -file ca.pem -alias 'CA' -keystore client.truststore.jks
```

Die referenzierten Zertifikate (service.key, service.cert, ca.pem) müssen von Aiven manuell heruntergeladen werden.

## MongoDB Atlas

Für das MongoDB Cluster muss ein Datenbank-User angelegt werden. dies geschieht via Terraform. Die Credentials für den
User müssen manuell erzeugt werden und via gitlab ci/cd Variable bereitgestellt werden.

## Gitlab-Variablen

die folgenden Variablen müssen manuell bei gitlab auf Service-Ebene hinterlegt werden:

- MONGO_TODO_APPLICATION_NAME_DB_HOST_<env>
- MONGO_TODO_APPLICATION_NAME_DB_USER_<env>
- MONGO_TODO_APPLICATION_NAME_DB_PASSWORD_<env>
- APPLICATION_NAME_SSL_KEYSTORE_PASSWORD_<env>
- APPLICATION_NAME_SSL_TRUSTSTORE_PASSWORD_<env>

## Sentry

Um Fehler zu registrieren und sichtbar zu machen nutzen wir Sentry. Sentry visualisiert die Fehler in einer
Online-Oberfläche. Die Besonderheit bei uns, ist es das wir GitLab dafür nutzen. Hierfür muss nur die
Dependency `implementation("io.quarkus:quarkus-logging-sentry")` hinzugefügt werden. Zusätzlich muss folgendes in
der `application.yml` angelegt werden:

```
quarkus:
  log:
    sentry:
      ~: true
      in-app-packages: de.blume2000
```

Zusätzlich muss noch die DSN per Umgebungsvariable in die GitLab CI hinzugefügt werden. Diese lautet `TF_VAR_sentry_dsn`
und hat den Inhalt der Error Tracking DSN aus GitLab. Die DSN wird automatisch von Terraform in der Backend Instanz
verfügbar gemacht.
