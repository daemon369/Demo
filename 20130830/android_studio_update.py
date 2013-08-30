#!/usr/bin/python
#--------------------------------------------------------------------------
# Name:        Android Studio Updater
# Purpose:     update android studio
#
# Author:      Daemon
# Created:     30-08-2013
#--------------------------------------------------------------------------
from os.path import basename
from urlparse import urlsplit
import urllib
import urllib2
import sys
import os
import platform

try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET

#更新信息XML文件
UPDATE_XML_URL = "https://dl.google.com/android/studio/patches/updates.xml"
#patch下载路径
PATCH_PATH = "http://dl.google.com/android/studio/patches/AI-"
VER_FROME = "130.737825"
VER_TO = "130.795381"
PATCH_PATH2_LINUX = "-patch-unix.jar"
PATCH_PATH2_WINDOWS = "-patch-win.jar"

#下载到本地的xml文件路径，更新后删除
LOCAL_XML_FILE_PATH = "./"
#下载到本地的patch文件，更新后删除
LOCAL_PATCH_FILE_PATH = "./"
FROM = ''
TO = ''

DOWNLOADED_PERCENT = 0

#通过URL截取文件名
def url2name(url):
    return basename(urlsplit(url)[2])

def downloadcb(a, b, c):
    """
        call back function
        a,已下载的数据块
        b,数据块的大小
        c,远程文件的大小
    """
    global DOWNLOADED_PERCENT
    percent = 100.0*a*b/c
    if percent - DOWNLOADED_PERCENT < 1.0:
    	  if percent < 99.0:
            return
    DOWNLOADED_PERCENT = percent
    if 100 < percent:
        percent = 100
    print "\r%.1f%%"%(percent,) + "          %d/%d"%(a*b,c)

def downloadfile(url,localFileName = None):
    localName = url2name(url)
    req = urllib2.Request(url)
    r = urllib2.urlopen(req)
    if localFileName:
        localName = localFileName
    f = open(localName, 'wb')
    f.write(r.read())
    f.close()
    return localName

def main():    
    PLATFORM = platform.system()

    if 0 == cmp(PLATFORM, "Linux"):
        PATCH_PATH2 = PATCH_PATH2_LINUX
    elif 0 == cmp(PLATFORM, "Windows"):
        PATCH_PATH2 = PATCH_PATH2_WINDOWS
    else:
        print "os not supported..."
        return
    
    localVerFile = open('build.txt', 'r')
    localVerStr = localVerFile.readline()
    localVerFile.close()
    VER_FROME = localVerStr[3:]

    localName = downloadfile(UPDATE_XML_URL)
    tree = ET.ElementTree(file=localName)
    root = tree.getroot()
    build = root[0][1][0]
    VER_TO = build.attrib.get('number')
    patchs = build.findall('patch')

    canUpdate = False
    for patch in patchs:
        from_ver = patch.attrib.get('from')
        if 0 == cmp(VER_FROME, from_ver):
            canUpdate = True
            break

    if False == canUpdate:
        print "cannot update from your local version:" + VER_FROME
        return

    patch_path = PATCH_PATH + VER_FROME + '-' + VER_TO + PATCH_PATH2
    print "Frome:" + VER_FROME
    print "To:" + VER_TO
    print "Patch URL:" + patch_path
    print "Starting Download ...\n"
    #patchFile = downloadfile(patch_path)
    localpatchfile = url2name(patch_path)
    urllib.urlretrieve(patch_path, localpatchfile, downloadcb)
    cmd = "java -classpath " + localpatchfile + " com.intellij.updater.Runner install ."
    os.system(cmd)
    pass

if __name__ == '__main__':
    main()
