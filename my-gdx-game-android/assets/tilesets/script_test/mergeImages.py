from os import listdir
import os.path
import sys
import getopt
from PIL import Image


#args = '-d -r -w'.split()

directory = None
newFileName = ""
newFilePath = None
filesInDir = []
fileIndicies = []
image = None
images = []
tmpImage = None
ext = ""
imageSize = ()
numOfTilesX = None #width in num of images
numOfTilesY = None #height in num of images
imageSpacing = 0
imageLoadingDone = False
imageCount = 0
tmpImageCount = 0
 
def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "d:n:x:s:", ["dir=", "name=", "ext=", "spacing="])
    except getopt.GetoptError as err:
        # print help information and exit:
        print str(err) # will print something like "option -a not recognized"
##        usage()
        sys.exit(2)
        
    global directory
    global newFileName
    global newFilePath
    global filesInDir
    global image
    global images
    global tmpImage
    global ext
    global imageSize
    global numOfTilesX
    global numOfTilesY
    global imageSpacing
    global imageLoadingDone
    global imageCount
    global tmpImageCount
    
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
##
    if(os.path.exists(newFilePath)):
        print "file %s already exists. Deleting.." %(newFilePath)
        os.remove(newFilePath)

    for f in os.listdir(directory):
        if(f.endswith(ext)):            
            if(not f.startswith("tmp_" + newFileName)): #remove tmp files
                if(not f.startswith(newFileName)):
                    filesInDir.append(f)
                else:
                    print "removing " + f
                    os.remove(os.path.join(directory, f))
            else:
                print "removing " + f
                os.remove(os.path.join(directory, f))

    filesInDir.sort()
    
    input = raw_input()

    while(imageCount < len(filesInDir)):
        print "imageCount: " + str(imageCount)
        print "load images"
        input = raw_input()
        loadImages(32)

        print "merge images"
        input = raw_input()
        mergeImages(imageSpacing, numOfTilesX, numOfTilesY, newFilePath)
        
        tmpImageCount += 1
        images = [] #clear the images list
        input = raw_input()


def loadImages(numOfImgToLoad):
    global imageCount
    global filesInDir
    global numOfTilesX
    global numOfTilesY
    global images
    global imageSize

    images = []
    imageFile = None
    i = imageCount
    for imageCount in range(len(filesInDir)):
        if filesInDir[i].endswith(ext):                
            fileIndex = []
            fileIndex.append(int(os.path.splitext(filesInDir[i])[0].split("-")[1]))

            fileIndex.append(int(os.path.splitext(filesInDir[i])[0].split("-")[2]))
            if(fileIndex[0] +1 > numOfTilesX):
                numOfTilesX = fileIndex[0] +1
            if(fileIndex[1] +1 > numOfTilesY):
                numOfTilesY = fileIndex[1] +1
                
            imageFile = open(os.path.join(directory, filesInDir[i]), "rb")
            image = Image.open(imageFile)
            image.load()
            imageFile.close()
##            image = Image.open(os.path.join(directory, filesInDir[i]))            
            imageSize = image.size

            images.append((fileIndex, image))

            print "i = %d, images= %d" %(i, len(images))
            if((i - imageCount) < numOfImgToLoad - 1): #only load 32 images at a time
                i += 1
            else:
                break

            imageCount += 1

       
def mergeImages(imageSpacing, numOfTilesX, numOfTilesY, newFilePath):
    global tmpImage
    global imageSize
    global images
    
##    tmpFileNames = [0]
    
    tmpImage = Image.new("RGBA", ((numOfTilesX * imageSize[0]) + (numOfTilesX * imageSpacing), (numOfTilesY * imageSize[1]) + numOfTilesY * imageSpacing))

    i = 0
    for y in range(numOfTilesY):
        for x in range(numOfTilesX):
            print "%d:%d" %(images[i][0][0],images[i][0][1])
            pos = ((x * imageSize[0]) +  (x * imageSpacing), (y * imageSize[1]) + (y *imageSpacing))
            print pos
            tmpImage.paste(images[i][1], pos)

            i += 1
##            print "x = %d, y = %d i = %d" %(x,y, i)

##    for f in filesInDir:
##        if(f.startswith("tmp_" + newFileName)):
##            tmpFileNames.append(f)
    tmpList = os.path.splitext(newFilePath)    
    tmpImage.save(tmpList[0] + "-" + str(tmpImageCount) + tmpList[1])

    print "clear image list"
    images = [] 

if __name__ == "__main__":
    main()
