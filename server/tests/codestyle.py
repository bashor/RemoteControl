import subprocess
import os

checker = "pep8"
project_root = "../.."
tornado_server = os.path.join(project_root, "server")

for root, dirs, files in os.walk(tornado_server):
    for f in files:
        full_name = os.path.join(root, f)
        if (full_name.endswith(".py")):
            subprocess.call([checker, full_name])
