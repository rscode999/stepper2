"""
Script to encrypt text using the Stepper (formerly Anti Kasinski Examination System) algorithm with revised shifts

Run with one additional command-line argument to take an input from a .txt file.
Run with with multiple additional command-line arguments to print the output to a .txt file.
Command-line arguments can be any word. Example: "python stepper.py aaaa" runs the script with one argument.

Operation:
1. Enter password
2. Enter plaintext. If running with additional command-line arguments, enter name of the input file
3. Decide whether to encrypt with punctuation, without spaces, or with only alphanumeric characters
4. Enter key
5. If running with multiple additional command-line arguments, enter name of the output file
6. Program prints the ciphertext and key

The program prints a warning if any characters in the ciphertext have non-printing Unicode values.
If warnings appear, decrypt the text to ensure proper encryption.
Even if there are no warnings, check the ciphertext for any non-printing characters

Chris P. Bacon
2 July 2024, Python 3.13.0b3 - 3.13.0b4
"""


from gc import collect as gc_collect
import sys
from stepper2_funcs import *


######################################
######################################

def encrNumbers(arr:list, key:str) -> list:
    """
    Returns a copy of `arr` that encrypts any numeric character in `arr` using `key`.

    Encryption uses modified Vigenere process. The 10 digits are taken to be the 'letters'.

    Any non-numeric character is treated as blank space.

    Parameter arr: the characters to encrypt
    Precondition: arr is a list of integers

    Parameter key: the key to encrypt with
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
            newChar=(newChar + (ord(key[keyIndex])-97) ) % 10
            newChar=(newChar + (ord(key[keyIndex])-97) ) % 10
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



def getCharFromString(msg="") -> str:
    """
    Prints `msg`, takes user input and returns the first non-space in the given input.
    If no non-spaces or empty string, returns a chr(0)

    Parameter msg: the message to ask the user for input
    Precondition: msg is a string
    """
    assert isinstance(msg, str), "message must be a string"

    inputStr=safeInput(msg)

    currentIndex=0
    while currentIndex < len(inputStr):
        if inputStr[currentIndex] != ' ':
            return inputStr[currentIndex]

        currentIndex=currentIndex+1

    return chr(0)


######################################
######################################



def _login(enteredPassword:str) -> int:
    """
    Self explanatory

    Parameter enteredPassword: password
    Precondition: enteredPassword is a string
    """
    assert isinstance(enteredPassword,str), "password must be a string"

    passwords=["", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "000000", "dragon", "jennifer", "12345", "123456", "abc123", "111111", "123123",
                        "sunshine", "BvtTest123", "11111", "ashley", "amanda", "justin", "cookie", "basketball", "shopping", "1qaz2wsx3edc", "thomas",
                        "123456789", "test1", "password", "12345678", "zinch", "g_czechout", "asdf", "qwerty", "1234567890", "1234567", "Aa123456.",
                        "iloveyou", "121212", "buster", "butterfly", "jordan", " ",
                        "password1", "monkey", "livetest", "55555", "family", "michael", "123321", "football", "baseball", "q1w2e3r4t5y6", "nicole",
                        "jessica", "purple", "shadow", "hannah", "chocolate", "michelle", "daniel", "maggie", "qwerty123", "hello", "112233", "tigger",
                        "666666", "987654321", "superman", "12345678910", "summer", "1q2w3e4r5t", "fitness", "bailey", "zxcvbnm", "fuckyou",
                        "soccer", "charlie", "asdfghjkl", "654321", "dubsmash", "1234", "00000", "princess", "test", "qwertyuiop"]

    passkeys=[True]*len(passwords)

    for p in range(0, len(passwords)):
        if not enteredPassword==passwords[p]:
            passkeys[p]=False


    cf=len(passwords)-1

    for c in range(0, len(passkeys)-1):
        if not passkeys[c]==True:
            cf=cf-1


    if cf<=0 and not passkeys[-1]==True:
        return 1

    elif cf>0 and not passkeys[-1]==True:
        return -1

    return 0


######################################
######################################



def recombineNumbers(textIn:list, numbersIn:list) -> list:
    """
    Returns a (list) copy of `textIn` but with all indices from `numbersIn` reinserted

    Similar to `recombineSymbols`, but for numbers and it returns a list

    `textIn` represents an output without non-alphabetic characters.
    Each index in text contains a numerical value for a letter.
    a=0, b=1, c=2... z=25

    `numbersIn` contains characters at every index where there would normally be
    a number in the original text, and a negative number otherwise

    Parameter textIn: the text to reinsert the numbers in
    Precondition: textIn is a list of ints

    Parameter numbersIn: the list of numbers to reinsert
    Precondition: numbersIn is a list of ints
    """
    assert isinstance(textIn,list) and isinstance(numbersIn,list), "both inputs must be lists"
    for v in textIn:
        if not isinstance(v,int):
            raise AssertionError("all indices in text must be ints")
    for v in numbersIn:
        if not isinstance(v,int):
            raise AssertionError("all indices in numbers must be ints")


    #Make a defensive copy of both inputs
    text=[]
    for a in textIn:
        text.append(a)
    numbers=[]
    for a in numbersIn:
        numbers.append(a)

    output=[]
    textIndex=0
    numbersIndex=0
    printLen=len(text)

    while numbersIndex < printLen:

        #If there's a number in the current index
        if 48<=numbers[numbersIndex]<=57:
            output.append(numbers[numbersIndex])
            numbers[numbersIndex]=-127

            printLen=printLen+1

        else:
            output.append(text[textIndex])
            textIndex=textIndex+1


        symbolsIndex=symbolsIndex+1

    return output


######################################
######################################



def removeSpaces(text:str) -> str:
    """
    Returns a copy of `text`, but without spaces.
    Exception: add a space after every punctuation mark, as defined in the 'punct' array
    unless the punctuation mark is the last character.

    If there are many punctuation marks in a row, add a space only after the last mark.

    Examples:
    "" >>> ""
    "abcabc" >>> "abcabc"
    "abc abc" >>> "abcabc"
    "abc 123 abc" >>> "abc123abc"
    "abc? Abc,abc!" >>> "abc? Abc, abc!"
    "abc!!-abc?!." >>> "abc!!- abc?!."

    DO NOT separate with a space if inside of a number.
    Example: "10,000 yards" >>> "10,000yards"

    Parameter text: the input text to remove spaces from
    Precondition: text is a string
    """
    assert isinstance(text,str), "input must be a string"

    punct=['.', ',', '!', '?', '-', ':', ';']


    output=""

    for currentIndex in range(len(text)):

        #After first index. If a punctuation mark was just passed, add a space
        if currentIndex>0 and not(text[currentIndex] in punct) and (text[currentIndex-1] in punct):
            #Do only if not inside of a number
            if ord(text[currentIndex-2])<48 or ord(text[currentIndex-2])>57:
                output=output + ' '

        #If current index is not a space, add it
        if text[currentIndex] != ' ':
            output=output + text[currentIndex]


        currentIndex=currentIndex+1

    return output





############################################################################
############################################################################
############################################################################
############################################################################




if __name__ == '__main__':


    ######################################
    #Security

    keyPosition=32767
    keyPosition=_login(safeInput("Enter password: "))

    if keyPosition > 0:
        print("**Incorrect password.")
        sys.exit(1)

    print("")


    ######################################
    #Receive PT and punctuation preferences


    #Take PT from the user, if there's no additional command-line arguments
    enteredPlaintext = ""
    if len(sys.argv) <= 1:
        enteredPlaintext=safeInput("Enter plaintext: ")

    #Otherwise, ask the user for a file input
    else:
        try:
            enteredPlaintext = readTextFromFile(safeInput("-Enter name of the input .txt file. Leave blank if using \"" + DEFAULT_INPUT_FILENAME + "\": "))

        #Something goes wrong with the file read
        except UnicodeDecodeError as ude:
            print("**Unsupported character found: " + str(ude))
            print("")
            sys.exit(1)

        #File doesn't exist, or incorrect filepath
        except FileNotFoundError:
            print("**The given file does not exist.")
            print("  Note: If not using a file in the same folder as this script, enter an absolute path to the file. Absolute paths start with a drive letter on Windows")
            print("  Example of absolute path: C:\\Users\\my_username\\Desktop\\my_text.txt")
            print("")
            sys.exit(1)

        #Not a .txt file
        except FileTypeError:
            print("**The input file must have the \".txt\" extension.")
            print("")
            sys.exit(1)



    #Ask user for punctuation
    punct=getCharFromString("   -Include punctuation? Enter 'y' for yes, 'n' for no, 's' to encrypt without spaces: ").lower()



    #enteredPlaintextalways leaves this section as a string


    ######################################
    #Receive the key


    #Take key input
    enteredKey=""
    enteredKey=enteredKey + safeInput("-Enter key: ")

    #Remove all non-alphabetic characters from the key
    enteredKey=removeNonAlphas(removeDiacritics(enteredKey))


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
    #Format the text and key
    gc_collect()

    #Loading screen
    if len(enteredPlaintext) > 75000:
        print(" Loading " + str(len(enteredPlaintext)) + " characters...")

    #Remove diacritics
    enteredPlaintext=removeDiacritics(enteredPlaintext)


    #If user wants without spaces, remove spaces from PT
    if punct=='s':
        enteredPlaintext=removeSpaces(enteredPlaintext)


    #Take non-alphabetic characters
    nonAlphas=getNonAlphaPositions(enteredPlaintext)

    #Remove non-alphabetic chars
    enteredPlaintext=removeNonAlphas(enteredPlaintext)


    #Remove non-alphas from key
    enteredKey = removeNonAlphas(removeDiacritics(enteredKey))

    #If key shorter than required length, add until length is the req'd length
    while len(enteredKey) < KEY_LENGTH:
        enteredKey = enteredKey + randChar()

    #If key longer than required length, cut off anything past the req'd length
    if len(enteredKey) > KEY_LENGTH:
        enteredKey=enteredKey[0:KEY_LENGTH]


    #Idiot check on the key
    if not len(enteredKey)==BLOCK_LENGTH*BLOCK_COUNT:
        raise Warning("Key is not the proper length")
    


    #################
    #Convert to blocks and prepare key for operation

    #Split PT and key into blocks. Formerly text and key
    textBlocks=createBlocks(enteredPlaintext)
    keyBlocks=createBlocks(enteredKey)



    #Represents the block positions while a block is not being encrypted. Formerly keyBlockPositions.
    keyBlockBasePositions=[0] * BLOCK_COUNT
    #This allows the key blocks to 'rotate' without changing the key block arrays

    #Represents the current index to read from in each key block. Formerly keyBlockOffsets.
    keyBlockReadPositions=[0] * BLOCK_COUNT

    # print(text)
    # print()
    # for p in range(len(key)):
    #     print(key[p])
    # print()

    ############################################################################
    #Go!

    if len(enteredPlaintext) > 75000:
        print(" Encrypting...")

    if keyPosition<0:
        sys.exit(0)
    gc_collect()

    #The CT and key are not strings. They are now arrays
    try:
        # print("Text length: " + str(len(text)*len(text[0]) + len(text[-1])))

        #Enc. all blocks except for the last one
        for b in range( len(textBlocks)-1 ):


            #Set starting positions of each key block
            for s in range(len(keyBlockBasePositions)):
                keyBlockReadPositions[s]=keyBlockBasePositions[s]


            #Enc. each character in the current block
            for c in range(BLOCK_LENGTH):
                # print("Enc. indices block " + str(b) + ", character " + str(c) + ": ", end='')

                #Move down each key block and encr.
                for k in range(BLOCK_COUNT):
                    textBlocks[b][c] = (textBlocks[b][c] + keyBlocks[k][keyBlockReadPositions[k]]) % 26


                # print()
                # print("Offsets: ",end='')
                # for p in keyBlockOffsets:
                #     print(p, end=' ')

                #Increment all offsets
                for i in range(len(keyBlockReadPositions)):
                    keyBlockReadPositions[i]=keyBlockReadPositions[i]+1
                    if keyBlockReadPositions[i]>=BLOCK_LENGTH:
                        keyBlockReadPositions[i]=0

                # print()
                # print("Offsets after incr: ",end='')
                # for p in keyBlockReadPositions:
                #     print(p, end=' ')

                # print()


            # print(keyBlockBasePositions)
            # print(b * BLOCK_LENGTH)


            #'Rotate' the key blocks forward by their specified increments
            for r in range(len(keyBlockBasePositions)):
                keyBlockBasePositions[r] = (keyBlockBasePositions[r] + KEY_BLOCK_INCREMENTS[r]) % BLOCK_LENGTH

            #If a period ends (i.e. BLOCK_LENGTH blocks have been encrypted since the last period end), step the key blocks forward
            if (b+1)%BLOCK_LENGTH==0:
                keyBlockBasePositions = setKeyBlockPositions(b+2)
                # print("PERIOD END DETECTED")



        #Set starting positions of each key block
        for s in range(len(keyBlockBasePositions)):
            keyBlockReadPositions[s]=keyBlockBasePositions[s]

        #Enc. the final block. The key's blocks should be fully rotated by now
        for c in range(len(textBlocks[-1])):

            #Move down each key block
            for k in range(BLOCK_COUNT):
                textBlocks[-1][c] = (textBlocks[-1][c] + keyBlocks[k][keyBlockReadPositions[k]]) % 26

            # for p in range(len(keyBlockOffsets)-1):
            #     print(keyBlockOffsets[p], end=', ')
            # print(keyBlockOffsets[len(keyBlockOffsets)-1])

            #Increment all offsets
            for i in range(len(keyBlockReadPositions)):
                keyBlockReadPositions[i]=keyBlockReadPositions[i]+1
                if keyBlockReadPositions[i]>=BLOCK_LENGTH:
                    keyBlockReadPositions[i]=0


        #Standard Vigenere encrypt the numbers using the original key
        nonAlphas=encrNumbers(nonAlphas,enteredKey)

    except KeyboardInterrupt:
        sys.exit(0)
    except:
        sys.exit(0)


    
    # print("Final key block positions: " +  str(keyBlockPositions))
    gc_collect()

    #Put all the non-alphabetic characters back
    outputText=recombineSymbols(flatten(textBlocks), nonAlphas)
    textBlocks = None
    keyBlocks = None

    
    print("")
    print("")

    
    #If text output is selected, print the output
    if len(sys.argv) <= 2:
        print("   --Ciphertext: ", end='')

        #If user wanted punctuation, print CT as-is
        if punct=='y' or punct=='s':
            print(outputText)
        #Otherwise, print only the alphanumeric characters
        else:
            for p in outputText:
                if p.isalnum():
                    print(p,end='')
            print("")

    #Otherwise, write the output to the selected file
    else:

        if not outputFile==None: #outputFile started as None
            try:
                outputFile = open(outputFile, "w")
                outputFile.write("")

                #If using punctuation, write as-is
                if punct=='y' or punct=='s':
                    outputFile.write(outputText)
                #Otherwise, print only the alphanumeric characters
                else:
                    for p in outputText:
                        if p.isalnum():
                            outputFile.write(p)

                outputFile.close()
                print("   --Output to \"" + outputFile.name + "\" succeeded.")

            except:
                print("**Output to \"" + outputFile.name + "\" failed.")
                sys.exit(1)
        
        else:
            print("**Internal error: check sys.argv comparisons")
            sys.exit(-1)


    #Check the text for weird characters
    for c in range(len(outputText)):
        if (ord(outputText[c])<32 and not 9<=ord(outputText[c])<=13) or ord(outputText[c])>255:
            print("*WARNING: Unsupported character '" + outputText[c] + "' (mapping " + str(ord(outputText[c])) +  ") found at position " + str(c+1) + " in the ciphertext. Encryption may not have proceeded properly.")


    #Print the key
    print("", flush=True)
    print("   --Key: " + enteredKey, flush=True)

    
    print("")
    print("", flush=True)
    sys.exit(0)
