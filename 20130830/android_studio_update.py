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
PATCH_PATH2 = ""
VER_FROM = "130.795381"
VER_TO = "132.809981"
PATH_LINUX = "unix"
PATH_WINDOWS = "win"
PATH_MAC = "mac"

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
    downloaded = a*b/1024
    total = c/1024
    print "\r%.1f%%"%(percent,) + "          %dk/%dk"%(downloaded,total)

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

def verifyos():
    #判断操作系统，目前支持Windows，Linux，MacOS
    global PATCH_PATH2
    PLATFORM = platform.system()
    if 0 == cmp(PLATFORM, "Linux"):
        PATCH_PATH2 = PATH_LINUX
    elif 0 == cmp(PLATFORM, "Windows"):
        PATCH_PATH2 = PATH_WINDOWS
    elif 0 == cmp (PLATFORM, "Darwin"):
        PATCH_PATH2 = PATH_MAC
    else:
        print "os not supported..."
        return False
    return True

def getdownloadurl(str_from, str_to, str_platform):
    path = PATCH_PATH + str_from + '-' + str_to + "-patch-" + str_platform + ".jar"
    return path

def main():
    if False == verifyos():
        return

    #读取本地Android Studio版本号
    localVerFile = open('build.txt', 'r')
    localVerStr = localVerFile.readline()
    localVerFile.close()
    VER_FROM = localVerStr[3:]

    #下载google提供的更新信息xml文件，读取可更新的版本号
    localUpdateXmlName = downloadfile(UPDATE_XML_URL)
    tree = ET.ElementTree(file=localUpdateXmlName)
    root = tree.getroot()
    build = root[0][1][0]
    VER_TO = build.attrib.get('number')
    patchs = build.findall('patch')

    #读取xml文件，判断本地Android Studio是否能更新
    canUpdate = False
    for patch in patchs:
        from_ver = patch.attrib.get('from')
        if 0 == cmp(VER_FROM, from_ver):
            canUpdate = True
            break

    if False == canUpdate:
        print "cannot update from your local version:" + VER_FROM
        return

    #下载URL
    patch_path = getdownloadurl(VER_FROM, VER_TO, PATCH_PATH2)
    print "Frome:" + VER_FROM
    print "To:" + VER_TO
    print "Patch URL:" + patch_path
    print "Start Download ...\n"

    localpatchfile = url2name(patch_path)

    #下载patch
    urllib.urlretrieve(patch_path, localpatchfile, downloadcb)

    #安装patch
    cmd = "java -classpath " + localpatchfile + " com.intellij.updater.Runner install ."
    os.system(cmd)

    os.remove(localUpdateXmlName)
    os.remove(localpatchfile)
    pass

if __name__ == '__main__':
    main()
