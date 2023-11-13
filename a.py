import requests


a = requests.post('https://10.29.1.231:4000/login', data={'username': 'xrd', 'password': 'secret'}, verify=False)

print(a.status_code)