# coding=utf-8
"""
Python client library for Lulu Cover Generation API (part of Publication API)
http://developer.lulu.com/docs/CoverGenerationAPI

"""

__licence__ = """
Copyright 2009-2010 Lulu, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you
may not use this file except in compliance with the License. You may
obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
    implied. See the License for the specific language governing
    permissions and limitations under the License.
"""
import sys
import time
import hashlib
import urllib, urllib2
import simplejson as json

cover_json = """
{
  "layoutDefinition" : {
    "name" : null,
    "coverSizeData" : {
      "fullCoverDimension" : null,
      "spineWidth" : {
        "valueInPoints" : 25.0,
        "valueInInches" : 0.35
      },
      "spineIndentation" : null
    }
  },
  "layoutBlocks" : {
    "frontCoverHeadline" : {
      "backgroundColor" : "#FFFFFF",
      "textColor" : "#000000",
      "text" : null
    },
    "title" : {
      "backgroundColor" : "#BBCBD3",
      "textColor" : "#000000",
      "text" : "%s"
    },
    "author" : {
      "backgroundColor" : "#BBCBD3",
      "textColor" : "#000000",
      "text" : "%s"
    },
    "spineAuthor" : {
      "backgroundColor" : "#88A0AC",
      "textColor" : "#000000",
      "text" : "%s"
    },
    "spineLogo" : {
      "imageFile" : null
    },
    "frontFlapText" : null,
    "backFlapText" : null,
    "authorBio" : {
      "backgroundColor" : "#FFFFFF",
      "textColor" : "#000000",
      "text" : null
    },
    "backCoverSynopsis" : {
      "backgroundColor" : "#FFFFFF",
      "textColor" : "#000000",
      "text" : null
    },
    "reviewBlurbs" : {
      "backgroundColor" : "#FFFFFF",
      "textColor" : "#000000",
      "text" : null
    },
    "publisherLogoFront" : {
      "imageFile" : "http://cfl.uploads.mrx.ca/ham/images/newser/2009/08/BobYoung5725.jpg"
    },
    "publisherLogoBack" : {
      "imageFile" : null
    },
    "frontFlapImage" : null,
    "backFlapImage" : null,
    "authorPhoto" : null,
    "spineTitle" : {
      "backgroundColor" : "#88A0AC",
      "textColor" : "#000000",
      "text" : "%s"
    },
    "backCoverBarCode" : null
  },
  "backgrounds" : {
    "frontCover" : {
      "value" : "#FFFFFF",
      "type" : "ColorCode"
    },
    "backCover" : {
      "value" : "#FFFFFF",
      "type" : "ColorCode"
    },
    "spine" : {
      "value" : "#FFFFFF",
      "type" : "ColorCode"
    },
    "frontFlap" : {
      "value" : "#FFFFFF",
      "type" : "ColorCode"
    },
    "backFlap" : {
      "value" : "#FFFFFF",
      "type" : "ColorCode"
    }
  }
}

"""
class InvalidInput(Exception):
  pass
class ServiceError(Exception):
  pass

class CoverGenServiceJSONClient:
  def __init__(self, api_key, service, version, method, templateCreatorId, templateId):
    host = "apps.lulu.com/api/pdfgen"
    self.url_format = "https://%s/%s/%s/%s/templateCreatorId/%s/templateId/%s" % (host, service, version, method, templateCreatorId, templateId)
    self.api_key = api_key

  def call(self, data, form_data=None):
    
    url_format = self.url_format
    api_key = self.api_key

    print url_format
    try:
      if form_data is None:
        form_data = {}
      form_data["api_key"]  = self.api_key
      form_data["data"] = data
      form_data = urllib.urlencode(form_data)
      print form_data
      
      req = urllib2.Request(url_format, form_data)
      req.add_header('CONTENT-TYPE', 'application/json')
      response = urllib2.urlopen(req).read()
      print response
    except urllib2.HTTPError, e:
      raise ServiceError("Error connecting to webservice. HTTP-%d: %s"
              % (e.code, e))
    except urllib2.URLError, e:
      raise ServiceError("Error connecting to webservice. %s" % e)
    
    try:
      response = json.loads(response)
    except (TypeError, ValueError, UnicodeError, AttributeError), e:
      raise ServiceError("Couldn't decode response from webservice. %s" %e)

    if response['status'] != 200:
      raise InvalidInput(response['details'])
    
    if response['taskStatus'] != 'COMPLETED':
      raise ServiceError(response['taskStatus'])

    return response['generatedPdfURI']

  def download(self, url, filename):
    handle = urllib2.urlopen(url)
    fd = open(filename, "w")
    while True:
      data = handle.read(4092)
      if data == "":
        break
      fd.write(data)
    fd.close()

key = "SET_KEY"

def main():      
  c = CoverGenServiceJSONClient(service='covers', version='v1', api_key=key, method = "generate", templateCreatorId="lulu", templateId="6x9dustjacket")
  title = "Revenge of the Albatross"
  author = "Stevenson"
  cjson = cover_json % (title, author, author, title)
  print 
  print
  print
  pdf_file = c.call(cjson)
  print pdf_file
  c.download(pdf_file, "foo.pdf")


if __name__ == "__main__":
    main()

