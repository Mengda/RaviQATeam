#!/usr/bin/python

import xml.etree.ElementTree as ET

def dfs(node,n):
    print ' ' * n, node.tag, '   ', node.attrib
    print ' ' * n, node.text
    for child in node:
        dfs(child,n+1)
tree = ET.parse("BMC_Syst_Biol_2009_Jul_31_3_79.nxml")
root = tree.getroot()
print root.tag
dfs(root,0)


