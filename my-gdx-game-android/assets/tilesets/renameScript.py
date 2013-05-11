from os import listdir
import os.path
import sys
import getopt
import re

args = '-d -r -w'.split()
                        
def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "d:r:w:x:", ["dir=", "replace=", "with=", "ext="])
    except getopt.GetoptError as err:
        # print help information and exit:
        print str(err) # will print something like "option -a not recognized"
##        usage()
        sys.exit(2)
    directory = None
    replaceStr = ""
    newStr = []
    oldStr = []
    verbose = False
    filesInDir = {}
    filePrefix = ""
    ext = ""
    for o, a in opts:
        if o in ("-d", "--dir"):
            directory = a
            print "dir: %s" %(directory)
        elif o in ("-r", "--replace"):
            replaceStr = a
        elif o in ("-w", "--with"):
            newStr = a
        elif o in ("-x", "--ext"):
            ext = a
        else:
            assert False, "unhandled option"

    if(not os.path.isdir(directory)):
        print "dir fail!"
        sys.exit(2)
    if(ext == ""):
        print "fail! no ext!"
        sys.exit(2)

    filesInDir = os.listdir(directory)
    filesInDir.sort()
    newFileList = []
    newFileName = []
    for f in filesInDir:
        if f.endswith("." + ext):
##            raw_input()
            
            tmp = os.path.splitext(f)[0]
            filePrefix, tmpOldStr = tmp.split("-", 1)
            oldStr = tmpOldStr.split("-")
            appendString = ""
            newFileName.append(filePrefix)
            
            if(len(oldStr[0]) < 2):                
                appendString = "0"
            else:
                appendString = ""
            newFileName.append(appendString + oldStr[0])
            
            if(len(oldStr[1]) < 2):
                appendString = "-0"
            else:
                appendString = ""
            newFileName.append(appendString + oldStr[1])

            newFileName.append("." + ext)
            
##            print(newFileName[0] + "-" + newFileName[1] + "-" + newFileName[2] + newFileName[3])
##            newFileList.append(newFileName[0] + "-" + newFileName[1] + "-" + newFileName[2] + newFileName[3])
            
            newFile = (newFileName[0] + "-" + newFileName[1] + "-" + newFileName[2] + newFileName[3])
            os.rename(os.path.join(directory, f), os.path.join(directory, newFile))
            newFileName = []
            print "renaming %s to %s" %(os.path.join(directory, f), os.path.join(directory, newFile))
            
    
    input = raw_input()

if __name__ == "__main__":
    main()
