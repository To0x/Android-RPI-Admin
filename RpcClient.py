'''
Created on 19.11.2013

@author: Master
'''

from xmlrpc.client import urllib
from xmlrpc.client import ServerProxy
from xml.sax.xmlreader import XMLReader

#from xmlrpc.client import ServerProxy



s = ServerProxy('http://localhost:9000')
print (s.system.listMethods())

print (s.add(1,2))
