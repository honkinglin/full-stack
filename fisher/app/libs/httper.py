import requests

class HTTP:
    @staticmethod
    def get(url, return_json=True, headers=None):
        r = requests.get(url, headers=headers)
        if r.status_code != 200:
            return {} if return_json else ''
        return r.json() if return_json else r.text
