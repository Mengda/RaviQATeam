# File Reader in Python
#
# Written by: Qichen Pan
#
# Date: 11/04/2013
#
# This program read through all files in your data path and output file names along with their keywords.


import os	
dataPath = raw_input("Please type in the path of your data: ")
dirs = [d for d in os.listdir(dataPath)]
FILES = []
KWDs = []
for d in dirs:
	files = [f for f in os.listdir(dataPath + d)] #if os.path.isfile(f)]
	for f in files:
		tmpFile = file(dataPath + d + '/' + f)
		FILES.append(f)
		kwdTuple = ()
		while True:
			line = tmpFile.readline()
			if len(line) == 0:
				break
			while True:
				lpos = line.find('<kwd>') + 5
				rpos = line.find('</kwd>')
				if rpos != -1:
					kwdTuple = kwdTuple + (line[lpos : rpos], )
				else: 
					break
				line = line[rpos + 1 :]
		KWDs.append(kwdTuple)	
	#break
outputFile = file('Output', 'w+')
for i in range(0, len(FILES)):
	outputFile.write(FILES[i] + '\t' + str(KWDs[i]) + '\n')
