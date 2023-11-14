import os
import time
# curl -X POST -u xrd:secret --location "https://localhost:4000/api/v1/api-keys" \
# --data "['XROAD_REGISTRATION_OFFICER','XROAD_SECURITY_OFFICER','XROAD_SYSTEM_ADMINISTRATOR','XROAD_MANAGEMENT_SERVICE']" \
# --header "Content-Type: application/json" \
# -k

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

# print("=============Kiem tra trang thai khoi tao CS=============")
# os.system('''
# curl -X GET --location \'https://{CS_IP}:4000/api/v1/initialization/status\' \
# --header \'Content-Type: application/json\' \
# --header \'Accept: application/json\' \
# --header \'Authorization: X-Road-ApiKey token={CS_API_KEY}\' \
# -k
# ''')
# print("\n========================================================")
# time.sleep(5)
# print("=============Khoi tao CS=============")
# os.system('''
# curl -X POST --location \'https://{CS_IP}:4000/api/v1/initialization\' \
# --header \'Content-Type: application/json\' \
# --header \'Accept: application/json\' \
# --header \'Authorization: X-Road-ApiKey token={CS_API_KEY}\' \
# --data-raw \'{
#   "central_server_address": "{CS_IP}",
#   "instance_identifier": "{CS_INSTANCE}",
#   "software_token_pin": "{PASSWORD}"
# }\' \
# -k
# ''')
#
# print("\n========================================================")
# time.sleep(5)
# print("=============Tao member classes=============")
# os.system('''
# curl -X POST --location "https://{CS_IP}:4000/api/v1/member-classes" \
# --header "Content-Type: application/json" \
# --header "Accept: application/json" \
# --header "Authorization: X-Road-ApiKey token={CS_API_KEY}" \
# --data \'{
#   "code": "{CS_MEMBER_CLASS}",
#   "description": "{CS_MEMBER_CLASS_DESC}"
# }\' \
# -k
# ''')
#
# print("\n========================================================")
# time.sleep(5)
# print("=============Them CA=============")
# os.system('''
# curl -X POST --location "https://{CS_IP}:4000/api/v1/certification-services" \
# --header "Content-Type: multipart/form-data" \
# --header "Accept: application/json" \
# --header "Authorization: X-Road-ApiKey token={CS_API_KEY}" \
# --form "certificate=@\"{PATH_TO_CA_CERT}\"" \
# --form "certificate_profile_info={CERTIFICATE_PROFILE_INFO}" \
# --form "tls_auth={TLS_AUTH_CA}" \
# -k
# ''')
#
# print("\n========================================================")
# time.sleep(5)
# print("=============Them OCSP Responder cho CA=============")
# os.system('''
# curl -X POST --location "https://{CS_IP}:4000/api/v1/certification-services/1/ocsp-responders" \
# --header "Content-Type: multipart/form-data" \
# --header "Accept: application/json" \
# --header "Authorization: X-Road-ApiKey token={CS_API_KEY}" \
# --form "certificate=@\"{PATH_TO_OCSP_CERT}\"" \
# --form "url=\"http://{CA_IP}:8888\"" \
# -k
# ''')
# print("\n========================================================")
# time.sleep(5)
# print("=============Them TSA=============")
# os.system('''
# curl -X POST --location "https://{CS_IP}:4000/api/v1/timestamping-services" \
# --header "Content-Type: multipart/form-data" \
# --header "Accept: application/json" \
# --header "Authorization: X-Road-ApiKey token={CS_API_KEY}" \
# --form "certificate=@\"{PATH_TO_TSA_CERT}\"" \
# --form "url=\"http://{CA_IP}:8899\"" \
# -k
# ''')
# print("\n========================================================")
# time.sleep(5)
# print("=============Them manage member=============")
# os.system('''
# curl --location 'https://%s:4000/api/v1/members' \
# --header 'Content-Type: application/json' \
# --header 'Accept: application/json' \
# --header 'Authorization: X-Road-ApiKey token=%s' \
# --data '{
#     "member_id": {
#         "member_class": "%s",
#         "member_code": "%s"
#     },
#     "member_name": "%s"
# }' \
# -k
# ''' % (CS_IP, CS_API_KEY, CS_MEMBER_CLASS, MANAGE_MEMBER_CODE, MANAGE_MEMBER_NAME))
# print("\n========================================================")
# time.sleep(5)
print("=============Them management subsystem=============")
os.system('''
curl -X POST --location 'https://%s:4000/api/v1/subsystems' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Authorization: X-Road-ApiKey token=%s' \
--data '{
  "subsystem_id": {
    "member_class": "%s",
    "member_code": "%s"
    "subsystem_code": "%s"
  }
}' \
-k
''' % (CS_IP, CS_API_KEY, CS_MEMBER_CLASS, MANAGE_MEMBER_CODE, MANAGE_MEMBER_SUBSYSTEM_CODE))
print("\n========================================================")
time.sleep(5)
# print("=============Them management service=============")
# os.system('''
# curl -X PATCH --location 'https://%s:4000/api/v1/management-services-configuration' \
# --header 'Content-Type: application/json' \
# --header 'Accept: application/json' \
# --header 'Authorization: X-Road-ApiKey token=%s' \
# --data '{
#   "service_provider_id": "%s:%s:%s:%s"
# }' \
# -k
# ''' % (CS_IP, CS_API_KEY, CS_INSTANCE, CS_MEMBER_CLASS, MANAGE_MEMBER_CODE, MANAGE_MEMBER_SUBSYSTEM_CODE))
print("\n========================================================")
time.sleep(5)
print("=============Dang nhap token=============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
print("============= =============")
os.system('''

''')
print("\n========================================================")
time.sleep(5)
