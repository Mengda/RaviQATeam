# File Reader in Python
#
# Written by: Qichen Pan
#
# Date: 11/08/2013
#
# This program read through all files in your data path and output file names along with their keywords.

import os
from xml.dom.minidom import parse
import xml.dom.minidom
import sys
reload(sys)
sys.setdefaultencoding("utf-8")

dataPath = raw_input("Please type in the path of your data: ")
outputFile = file('output', 'w+')
for root, dirs, files in os.walk(dataPath, topdown=False):
    for name in files:
        filePath = os.path.join(root, name)
	DOMTree = xml.dom.minidom.parse(filePath)
	collection = DOMTree.documentElement
	kwds = collection.getElementsByTagName("kwd")
	if len(kwds) > 0:
		outputList = filePath
		for kwd in kwds:
			if len(kwd.childNodes) > 0:
				outputList += "#" + str(kwd.childNodes[0].nodeValue)
		outputFile.write(outputList + '\n')
       	 

