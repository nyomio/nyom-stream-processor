#!/usr/bin/env python
import pycurl
from io import StringIO, BytesIO
import json
import sys

flinkHost = sys.argv[1]


def getrunningjobs():
    response = BytesIO()
    c = pycurl.Curl()
    c.setopt(c.URL, flinkHost + "/jobs/overview")
    c.setopt(c.WRITEFUNCTION, response.write)
    c.perform()
    c.close()
    a = json.loads(response.getvalue().decode('UTF-8'))
    return a['jobs']


def findjobidbyname(jobs, name):
    for job in jobs:
        if job['name'] == name and job['state'] == "RUNNING":
            return job['jid']
    return None


def uploadjar(jarfile):
    response = BytesIO()
    c = pycurl.Curl()
    c.setopt(c.URL, flinkHost + "/jars/upload")
    c.setopt(c.HTTPPOST, [('jarfile=@', (pycurl.FORM_FILE, jarfile))])
    c.setopt(c.WRITEFUNCTION, response.write)
    c.perform()
    c.close()
    a = json.loads(response.getvalue().decode('UTF-8'))
    return a['filename'].split("/")[4]


def deletejar(jarfile):
    response = BytesIO()
    c = pycurl.Curl()
    c.setopt(c.URL, flinkHost + "/jars/" + jarfile)
    c.setopt(c.CUSTOMREQUEST, 'DELETE')
    c.setopt(c.WRITEFUNCTION, response.write)
    c.perform()
    c.close()


def startjob(file_id):
    response = BytesIO()
    c = pycurl.Curl()
    c.setopt(c.URL, flinkHost + "/jars/" + file_id + "/run")
    c.setopt(c.CUSTOMREQUEST, 'POST')
    c.setopt(c.WRITEFUNCTION, response.write)
    c.perform()
    c.close()
    print(response.getvalue().decode('UTF-8'))


def canceljob(job_id):
    response = BytesIO()
    c = pycurl.Curl()
    c.setopt(c.URL, flinkHost + "/jobs/" + job_id)
    c.setopt(c.CUSTOMREQUEST, 'PATCH')
    c.setopt(c.WRITEFUNCTION, response.write)
    c.perform()
    c.close()

if __name__ == "__main__":
    fileid = uploadjar(sys.argv[2])
    jobid = findjobidbyname(getrunningjobs(), "nyomjob")
    if jobid is None:
        startjob(fileid)
    else:
        canceljob(jobid)
        startjob(fileid)
