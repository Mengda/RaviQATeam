# A constructor that can build a key dictionary                             
#
# Written by: Qichen Pan
#
# Date: 11/08/2013
#
# This program can build a huge dictionary containing all keys and their related articles.

import os
from xml.dom.minidom import parse
import xml.dom.minidom
from collections import OrderedDict
import sys
reload(sys)
sys.setdefaultencoding("utf-8")

dictKey = {}

dataPath = raw_input("Please type in the path of your data: ")
outputFile = file('KeyDict', 'w+')
for root, dirs, files in os.walk(dataPath, topdown=False):
    for name in files:
        filePath = os.path.join(root, name)
	tmpFile = file(filePath)
	while True:
		line = 	tmpFile.readline().strip()
		if len(line) == 0:
			break
		Token = line.split("#")
		for tk in Token[1 : ]:
			if dictKey.has_key(tk):
				dictKey[tk] = dictKey[tk] + (Token[0],)
			else:
				dictKey[tk] = (Token[0],)
dictKey = sorted(dictKey.items(), key=lambda x: x[0])
for elements in dictKey:
	outputFile.write(str(elements[0].strip()) + "\t" + "#".join(map(str, elements[1])) + '\n')
			

