CS_API_KEY="a60692df-94eb-4438-b05a-1ff61ecbc106"
CS_IP="10.231.216.99"
CA_IP="10.231.216.207"
PASSWORD="ript!@!2023"
CS_INSTANCE="CS"
CS_MEMBER_CLASS="GOV"
CS_MEMBER_CLASS_DESC="Governmental"
CERTIFICATE_PROFILE_INFO="ee.ria.xroad.common.certificateprofile.impl.FiVRKCertificateProfileInfoProvider"
TLS_AUTH_CA="false"
PATH_TO_CA_CERT="/home/ubuntu/ss_adapter/certs/ca_cert.pem"
PATH_TO_OCSP_CERT="/home/ubuntu/ss_adapter/certs/ocsp_cert.pem"
PATH_TO_TSA_CERT="/home/ubuntu/ss_adapter/certs/tsa_cert.pem"
MANAGE_MEMBER_CODE="MANAGESSMC"
MANAGE_MEMBER_NAME="MANAGESSN"
MANAGE_MEMBER_SUBSYSTEM_CODE="MANAGEMENT"

curl -X POST --location 'https://10.231.216.99:4000/api/v1/subsystems' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Authorization: X-Road-ApiKey token=a60692df-94eb-4438-b05a-1ff61ecbc106' \
--data '{
  "subsystem_id": {
    "member_class": "GOV",
    "member_code": "MANAGESSMC"
    "subsystem_code": "MANAGEMENT"
  }
}' \
-k
