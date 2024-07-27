"""
Stepper (formerly Anti Kasinski Examination System) decoder script. Uses revised shifts 

Run with one additional command-line argument to take an input from a .txt file.
Run with with multiple additional command-line arguments to print the output to a .txt file.
Command-line arguments can be any word. Example: "python stepper-d.py abcde" runs the script with one argument.

Operation:
1. Enter password
2. Enter ciphertext. If running with additional command-line arguments, enter name of the input file
3. Enter key
4. If running with multiple additional command-line arguments, enter name of the output file
5. Program prints the plaintext and key

The program prints a warning if any characters in the ciphertext have non-printing Unicode values.
If warnings appear, the text may not have properly decrypted.

Chris P. Bacon
2 July 2024, Python 3.13.0b3 - 3.13.0b4
"""


from gc import collect as gc_collect
import sys
from stepper2_funcs import *


######################################
######################################

def decr(ct:int, key:int) -> int:
    """
    Returns the decrypted version of `ct` using `key`.

    `ct` and `key` are NOT CHARACTERS! They are numerical values!

    This function DOES NOT ENFORCE PRECONDITIONS!

    Parameter ct: ciphertext character to decrypt
    Precondition: ct is an integer on [0, 25]

    Parameter key: key character to decrypt with
    Precondition: key is an integer on [0, 25]
    """
    output=(ct - key) % 26

    if output<0:
        output=output + 26

    return output


######################################
######################################



def decrNumbers(arr:list, key:str) -> list:
    """
    Returns a copy of `arr` that decrypts any numeric character in arr using `key`.

    The values in `arr` are Unicode values as integers, not characters.

    Decryption uses modified Vigenere process. Any non-numeric character,
    i.e. any character c whose Unicode value is not on the interval [48, 57] is treated as blank space.

    Parameter arr: the characters to decrypt
    Precondition: arr is a list of integers

    Parameter key: the key to decrypt with
    Precondition: key is a string of all lowercase English letters
    """
    assert isinstance(arr,list), "input array must be a list"
    for v in arr:
        if isinstance(v,int)==False:
            raise AssertionError("all indices in input array must be ints")

    assert isinstance(key,str), "key must be a string"
    for v in key:
        if ord(v)<97 or ord(v)>122:
            raise AssertionError("the entire key must be lowercase letters")

    output=[]

    keyIndex=0
    newChar=chr(0)

    for i in range(len(arr)):
        if 48<=arr[i]<=57:
            newChar=arr[i]-48

            newChar=(newChar - (ord(key[keyIndex])-97) ) % 10
            if newChar<0:
                newChar=newChar + 10
            newChar=(newChar - (ord(key[keyIndex])-97) ) % 10
            if newChar<0:
                newChar=newChar + 10

            newChar=newChar+48

            output.append(newChar)

            keyIndex=keyIndex+1
            if keyIndex >= len(key):
                keyIndex=0
        else:
            output.append(arr[i])

    return output


######################################
######################################



def initializeKeyBlockPositions(textLen:int) -> list:
    """
    Returns an array of ints representing the key block positions at the end of encryption,
    if the input was `textLen` chars long

    The value at index 0 represents the position of the topmost key block. 
    Position BLOCK_COUNT-1 represents the position of the bottommost key block. 

    Parameter textLen: the length of the ciphertext
    Precondition: textLen is a non-negative integer
    """
    assert isinstance(textLen,int) and textLen>=0, "textLen must be a non-negative integer"

    #Calculate how many blocks are in the text
    textBlocks = textLen // BLOCK_LENGTH
    if textLen%BLOCK_LENGTH==0:
        textBlocks -= 1


    #Set the block positions to the start of encryption
    output = [0] * BLOCK_COUNT

    #Simulate moving through each block of the text
    for b in range(textBlocks):
        
        #Increment the output
        for r in range(len(output)):
            output[r] = (output[r] + KEY_BLOCK_INCREMENTS[r]) % BLOCK_LENGTH
        
        #Step the key blocks if a period (passes BLOCK_LENGTH blocks) ends
        if (b+1)%BLOCK_LENGTH==0:
            output = setKeyBlockPositions(b+2)
            # print("PERIOD END DETECTED")

        # print(output)

    return output


######################################
######################################



def _login(enteredPassword:str) -> int:
    """
    Self explanatory

    Parameter enteredPassword: password
    Precondition: enteredPassword is a string
    """
    assert isinstance(enteredPassword,str), "password must be a string"

    passwords=["jessica", "maggie", "qwerty123", "hello", "112233", "tigger", "michelle", "daniel", "hannah", "shadow", "chocolate", 
        "sunshine", "BvtTest123", "11111", "ashley", "amanda", "justin", "cookie", "basketball", "shopping", "1qaz2wsx3edc", "thomas",
                        "123456789", "test1", "password", "12345678", "zinch", "g_czechout", "asdf", "qwerty", "1234567890", "1234567", "Aa123456.",
                        "iloveyou", "121212", "buster", "butterfly", "jordan", " ",
                        "password1", "monkey", "livetest", "55555", "family", "michael", "123321", "football", "baseball", "q1w2e3r4t5y6", "nicole",
                        "666666", "987654321", "superman", "12345678910", "summer", "1q2w3e4r5t", "fitness", "bailey", "zxcvbnm", "fuckyou",
                        "soccer", "charlie", "asdfghjkl", "654321", "00000", "000000", "dubsmash", "test", "princess", "1234", "qwertyuiop",
                        "", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "dragon", "12345", "123456", "123123", "jennifer", "abc123", "111111"]

    passkeys=[True]*len(passwords)

    for p in range(0, len(passwords)):
        if not enteredPassword==passwords[p]:
            passkeys[p]=False


    cf=len(passwords)-1

    for c in range(1, len(passkeys)):
        if not passkeys[c]==True:
            cf=cf-1


    if cf<=0 and not passkeys[0]==True:
        return 1

    elif cf>0 and not passkeys[0]==True:
        return -1

    return 0




############################################################################
############################################################################
############################################################################
############################################################################




if __name__ == "__main__":


    ######################################
    #Security

    ind=32767
    ind=_login(safeInput("Enter password: "))
    if ind > 0:
        print("**Incorrect password.")
        sys.exit(1)

    print("")



    ######################################
    #Receive CT


    #Take CT from the user, if there's no additional command-line arguments
    enteredCiphertext = ""
    if len(sys.argv)<=1:
        enteredCiphertext=safeInput("-Enter ciphertext: ")
    
    #Otherwise, ask the user for a file path
    else:
        try:
            enteredCiphertext = readTextFromFile(safeInput("-Enter name of the input .txt file. Leave blank if using \"" + DEFAULT_INPUT_FILENAME + "\": "))

        #Something goes wrong with the file read
        except UnicodeDecodeError as ude:
            print("**Unsupported character found: " + str(ude))
            print("")
            sys.exit(1)

        #Not a .txt file
        except FileTypeError:
            print("**The input file must have the \".txt\" extension.")
            print("")
            sys.exit(1)

        #File doesn't exist, or incorrect filepath
        except FileNotFoundError:
            print("**The given file does not exist.")
            print("  Note: If not using a file in the same folder as this script, enter an absolute path to the file. Absolute paths start with a drive letter on Windows")
            print("  Example of absolute path: C:\\Users\\username\\Desktop\\my_text.txt")
            print("")
            sys.exit(1)

    
    #enteredCiphertext always leaves this section as a string


    ######################################
    #Receive key

    enteredKey=safeInput("-Enter key: ")



    ######################################
    #Receive the output file

    outputFile = None

    if len(sys.argv) > 2:

        #Take file name from the user.
        outputFile = safeInput("-Enter name of the output file. Leave blank if using \"" + DEFAULT_OUTPUT_FILENAME + "\": ")
 
        #If empty, send result to default output file
        if len(outputFile) <= 0:
            outputFile = DEFAULT_OUTPUT_FILENAME
        #If not and the given name doesn't have the .txt extension, append it
        elif len(outputFile)<4 or not outputFile[-4:]==".txt":
            outputFile = outputFile + ".txt"
        

        #Temporarily convert outputFile into a File object.
        outputFile = open(outputFile, "a")
        outputFile.close()

        #Turn outputFile back into its name
        outputFile = outputFile.name





    ######################################
    #Format everything
    gc_collect()

    if (len(enteredCiphertext))>75000:
        print(" Loading " + str(len(enteredCiphertext)) + " characters...")


    #Process the CT

    enteredCiphertext=removeDiacritics(enteredCiphertext)

    #Save the CT's non-alphabetic characters in another array
    nonAlphas=getNonAlphaPositions(enteredCiphertext)
    #Remove non-alphabetic characters
    enteredCiphertext=removeNonAlphas(enteredCiphertext)


    #Process the key

    enteredKey=removeDiacritics(enteredKey)
    enteredKey=removeNonAlphas(enteredKey)

    #Add random characters to the key until it's as long as required
    while len(enteredKey) < KEY_LENGTH:
        enteredKey=enteredKey + randChar()

    #Remove all characters past the required length
    enteredKey=enteredKey[0:KEY_LENGTH]


    # print(enteredCiphertext)
    # print(enteredKey)

    ###########



    #Convert CT and key to blocks (turns them into arrays). Formerly text and key
    textBlocks=createBlocks(enteredCiphertext) 
    keyBlocks=createBlocks(enteredKey)

    # print(text)
    # print()
    # for p in range(len(key)):
    #     print(key[p])
    # print()


    #Set key block positions (formerly keyBlockPositions)
    keyBlockBasePositions=initializeKeyBlockPositions(len(enteredCiphertext))


    #Set the current index to read from in each key block (formerly keyBlockOffsers)
    keyBlockReadPositions=[]
    for a in range(BLOCK_COUNT):
        keyBlockReadPositions.append(0)



    ############################################################################
    #Go!

    if len(enteredCiphertext) > 75000:
        print(" Decrypting...")

    if ind<0:
        sys.exit(0)
    gc_collect()

    #The CT and key are not strings. They are now arrays
    
    try:
        #Decrypt the numbers in the nonAlphas array
        nonAlphas=decrNumbers(nonAlphas,enteredKey)

        # print("Initial key block positions: " + str(keyBlockPositions))
        # print("Text length: " + str(len(text)*len(text[0]) + len(text[-1])))


        #Set starting offsets at the position at the end of encryption

        #Start each block offset at the current position
        for s in range(len(keyBlockBasePositions)):
            keyBlockReadPositions[s]=keyBlockBasePositions[s]
        #Increment each offset for each encrypted final block character
        for s in range(len(textBlocks[-1])):
            for i in range(len(keyBlockReadPositions)):
                keyBlockReadPositions[i]=keyBlockReadPositions[i]+1
                if keyBlockReadPositions[i]>=BLOCK_LENGTH:
                    keyBlockReadPositions[i]=0

        # for pos in range(len(keyBlockPositions)):
        #     if keyBlockPositions[pos]>0:
        #         for corr in range(pos):
        #             keyBlockOffsets[corr]=keyBlockOffsets[corr]-1
        #         break



        #Decr. the final block, using the fully rotated key block offsets
        #Start from the index farthest to the right


        #Decr. each character in the block, starting from the bottom
        for c in range(len(textBlocks[-1])-1, -1, -1):

            #Decrement all offsets
            for i in range(len(keyBlockReadPositions)):
                keyBlockReadPositions[i]=keyBlockReadPositions[i]-1
                if keyBlockReadPositions[i]<0:
                    keyBlockReadPositions[i]=BLOCK_LENGTH-1


            # print("Key block offsets: ", end='')
            # for p in range(len(keyBlockOffsets)-1):
            #     print(keyBlockOffsets[p], end=', ')
            # print(keyBlockOffsets[len(keyBlockOffsets)-1])

            #Move up each key block, starting from the bottommost block
            for k in range(BLOCK_COUNT-1, -1, -1):
                textBlocks[-1][c] = decr(textBlocks[-1][c], keyBlocks[k][keyBlockReadPositions[k]])


        # print()
        # print()


        #Move through the remaining blocks, starting from the rightmost block
        for b in range(len(textBlocks)-2, -1, -1):

            
            #If a period ends (i.e. BLOCK_LENGTH blocks have been decrypted since the last period end), step the key blocks backward
            if (b+1)%BLOCK_LENGTH == 0:
                keyBlockBasePositions = setKeyBlockPositions(b)
                # print("PERIOD END DETECTED")


            #'Rotate' the key blocks backward by their specified decrements
            for r in range(len(keyBlockBasePositions)):

                keyBlockBasePositions[r] = keyBlockBasePositions[r] - KEY_BLOCK_INCREMENTS[r]
                
                if keyBlockBasePositions[r] < 0:
                    keyBlockBasePositions[r] += BLOCK_LENGTH
            

            # print(keyBlockPositions)


            # print("Initial key block offsets: ", end='')
            # for p in range(len(keyBlockOffsets)-1):
            #     print(keyBlockOffsets[p], end=', ')
            # print(keyBlockOffsets[len(keyBlockOffsets)-1])

            #Set starting positions for each key block
            for s in range(len(keyBlockBasePositions)):
                keyBlockReadPositions[s]=keyBlockBasePositions[s]

            #Decr. each character in the current block, starting from the bottom
            for c in range(BLOCK_LENGTH-1, -1, -1):

                #Decrement all offsets
                for i in range(len(keyBlockReadPositions)):
                    keyBlockReadPositions[i]=keyBlockReadPositions[i]-1
                    if keyBlockReadPositions[i]<0:
                        keyBlockReadPositions[i]=BLOCK_LENGTH-1

                # print("Key block offsets for block index " + str(c) + ": ", end='')
                # for p in range(len(keyBlockOffsets)-1):
                #     print(keyBlockOffsets[p], end=', ')
                # print(keyBlockOffsets[len(keyBlockOffsets)-1])

                #Move from right to left in each key block and decr. the current character
                for k in range(BLOCK_COUNT-1, -1, -1):
          
                    textBlocks[b][c] = decr(textBlocks[b][c], keyBlocks[k][keyBlockReadPositions[k]])

    except KeyboardInterrupt:
        sys.exit(0)
    except:
        sys.exit(0)


    #Reinsert non-alphabetic characters into the text
    outputText=recombineSymbols(flatten(textBlocks), nonAlphas)
    textBlocks = None
    keyBlocks = None

    print("")
    print("")
    gc_collect()


    #If console output is chosen, output the results to stdout
    if len(sys.argv) <= 2:
        print("   --Plaintext: " + outputText)

    #If file output is chosen, print the results to the chosen output file
    else:
        if not outputFile==None:
            try:
                outputFile = open(outputFile, "w")
                outputFile.write(outputText)
                print("   --Output to \"" + outputFile.name + "\" succeeded.")
                outputFile.close()

            except OSError:
                print("**Output to \"" + outputFile.name + "\" failed. The file does not exist.")
                sys.exit(1)


    #Check the text for weird characters
    for c in range(len(outputText)):
        if (ord(outputText[c])<32 and not 9<=ord(outputText[c])<=13) or ord(outputText[c])>255:
             print("*WARNING: Unsupported character '" + outputText[c] + "' (mapping " + str(ord(outputText[c])) +  ") found at position " + str(c+1) + " in the plaintext. Decryption may not have succeeded.")


    #Print the key
    print("", flush=True)
    print("   --Key: " + enteredKey, flush=True)


    print("")
    print("", flush=True)
    sys.exit(0)
