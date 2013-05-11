from os import listdir
import os.path
import sys
import getopt
from PIL import Image


#args = '-d -r -w'.split()
 
def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "d:n:x:s:", ["dir=", "name=", "ext=", "spacing="])
    except getopt.GetoptError as err:
        # print help information and exit:
        print str(err) # will print something like "option -a not recognized"
##        usage()
        sys.exit(2)
    directory = None
    newFileName = ""
    newFilePath = None
    filesInDir = []
    fileIndicies = []
    image = None
    images = []
    blankImage = None
    ext = ""
    imageSize = ()
    numOfTilesX = None #width in num of images
    numOfTilesY = None #height in num of images
    imageSpacing = 0
    for o, a in opts:
        if o in ("-d", "--dir"):
            directory = a
            print "dir: %s" %(directory)
        elif o in ("-n", "--name"):
            newFileName = a            
        elif o in ("-x", "--ext"):
            ext = "." + a
        elif o in ("-s", "--spacing"):
            imageSpacing = int(a)
        else:
            assert False, "unhandled option"

    if(not os.path.isdir(directory)):
        print "dir fail!"
        sys.exit(2)
    if(ext == ""):
        print "fail! no ext!"
        sys.exit(2)
    if(newFileName != "" and newFileName != None):
        newFilePath = os.path.join(directory, newFileName + ext)
    if(os.path.exists(newFilePath)):
        print "file %s already exists. Deleting.." %(newFilePath)
        os.remove(newFilePath)
        

    filesInDir = os.listdir(directory)
    filesInDir.sort()
    
    for f in filesInDir:
##        print f
        if f.endswith(ext):
##            raw_input()
            fileIndex = []
            fileIndex.append(int(os.path.splitext(f)[0].split("-")[1]))

            fileIndex.append(int(os.path.splitext(f)[0].split("-")[2]))
            if(fileIndex[0] +1 > numOfTilesX):
                numOfTilesX = fileIndex[0] +1
            if(fileIndex[1] +1 > numOfTilesY):
                numOfTilesY = fileIndex[1] +1
                
            fileIndicies.append(fileIndex)
            image = Image.open(os.path.join(directory, f))
            imageSize = image.size
            images.append((fileIndex, image))
##            print imageSize

    blankImage = Image.new("RGBA", (256,256))#(numOfTilesX * imageSize[0], numOfTilesY * imageSize[1]))

    i = 0
    for y in range(numOfTilesY):
        for x in range(numOfTilesX):
            print imageSpacing
            print "%d:%d" %(images[i][0][0],images[i][0][1])
            blankImage.paste(images[i][1], ((x * imageSize[0]) + imageSpacing, (y * imageSize[1]) + imageSpacing))
            i += 1
            print "x = %d, y = %d i = %d" %(x,y, i)

    blankImage.save(newFilePath)
    
    input = raw_input()

if __name__ == "__main__":
    main()
