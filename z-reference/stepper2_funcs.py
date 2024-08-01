"""
Module with useful constants and functions for Stepper scripts

No function should modify its input, unless otherwise specified.
Any modifications to inputs should occur on defensive copies.

Chris P. Bacon
Original 26-29 February 2024, Python 3.11.7 - 3.12.4
Revised 2 July 2024, Python 3.13.0b3 - Python 3.13.0b4
"""

from random import randint as rand_randint
from secrets import randbelow as secr_randbelow
import sys



DEFAULT_INPUT_FILENAME = "input.txt"
DEFAULT_OUTPUT_FILENAME = "output.txt"

BLOCK_LENGTH=19 #key matrix "width"
BLOCK_COUNT=7 #key matrix "height"
KEY_LENGTH=BLOCK_COUNT*BLOCK_LENGTH

KEY_BLOCK_INCREMENTS = [2,3,5,7,11,13,17]


#Do idiot checks

assert (isinstance(DEFAULT_INPUT_FILENAME,str) 
        and len(DEFAULT_INPUT_FILENAME)>=4
        and DEFAULT_INPUT_FILENAME[-4:]==".txt"), "default input file must be a string ending in \".txt\""

assert (isinstance(DEFAULT_OUTPUT_FILENAME,str) 
        and len(DEFAULT_OUTPUT_FILENAME)>=4
        and DEFAULT_OUTPUT_FILENAME[-4:]==".txt"), "default output file must be a string ending in \".txt\""

assert type(BLOCK_COUNT)==int and type(BLOCK_LENGTH)==int, "key dimensions must be ints"
assert BLOCK_COUNT>0 and BLOCK_LENGTH>0, "key dimensions must be positive"

assert len(KEY_BLOCK_INCREMENTS) == BLOCK_COUNT, "length of key block increments and block count must be equal"
for e in KEY_BLOCK_INCREMENTS:
    assert type(e)==int and e>=0, "every element of the key block increment list must be a non-negative integer"


######################################
######################################
######################################


class FileTypeError(ValueError):
    """
    A required file extension is not present.

    Inherits from ValueError.
    """
    pass


######################################
######################################



def createBlocks(input:str, blockLen:int=BLOCK_LENGTH) -> list:
    """
    Returns an array of int-arrays containg pieces, of length `blockLen`, of `input`.
    Each character in the output should be the input character's numeric value.
    Numeric values: a=0, b=1, c=2, d=3... z=25

    If an array in the output array reaches length `blockLen`,
    another array should be appended to the output and given the subsequent characters.

    Examples: if `blockLen`=16:
    "x" >>> [ [23] ]
    "azy" >>> [ [0,25,24] ]
    "abcdefghijklmnopq" >>> [ [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15], [16] ]

    If given the empty string, createBlocks should return an empty nested list
    "" >>> [[]]

    Parameter input: the string to split into blocks
    Precondition: input is a string containing only lowercase ASCII letters

    Parameter blockLen: the length of each subarray
    Precondition: blockLen is a positive integer
    """
    assert isinstance(input,str), "input must be a string"
    for v in input:
        if not ( 97<=ord(v)<=122 ):
            raise AssertionError("input must contain lowercase letters only whose Unicode values are on the interval [97,122]")

    assert isinstance(blockLen,int) and blockLen>0, "block length must be a positive integer"

    output=[[]]
    if len(input) <= 0:
        return output

    #Loop through all input chars that would not form part of the last array index
    inputIndex=0
    for b in range(len(input) // blockLen):
        #Append another string to the output and add the next blockLen chars to it
        output.append([])
        for c in range(blockLen):
            output[b].append( ord(input[inputIndex])-97 )
            inputIndex=inputIndex+1

    #Append all remaining chars to the final output index
    for c in range(len(input) % blockLen):
        output[-1].append( ord(input[inputIndex])-97 )
        inputIndex=inputIndex+1


    #Remove the last index if it's empty
    if len(output[-1])<=0:
        output.pop(-1)

    return output


######################################
######################################



def flatten(arr:list) -> list:
    """
    Returns a 1-D copy of `arr`

    Order moves from left to right, top to bottom.
    If `arr` is:
    [[1,2,3,4],
     [5,6,7,8],
     [9]]
    output would be [1,2,3,4,5,6,7,8,9]

    Parameter arr: the array to flatten
    Precondition: arr is a 2-D array of integers without negative numbers
    """
    _flattenAssert(arr)

    output=[]
    for a in range(len(arr)):
        for i in range(len(arr[a])):
            output.append(arr[a][i])

    return output

############

def _flattenAssert(arr):
    """
    Asserts the preconditions for the `flatten` function

    Parameter arr: the input to check.
    Precondition: none
    """
    assert isinstance(arr, list), "Input must be a 2-D list"
    for v in range(len(arr)):

        assert isinstance(arr[v], list), "All indices of the input must be lists"

        for w in range(len(arr[v])):
            assert type(arr[v][w])==int and arr[v][w]>=0, "Input must be 2-D array of non-negative ints"


######################################
######################################



def getNonAlphaPositions(input:str) -> list:
    """
    Returns an array of integers
    whose values are `input`'s non-alphabetic chars as a Unicode value

    At all indices with alphabetic characters, the output has a negative number at that index

    An alphabetic character is a letter in the English alphabet
    whose Unicode value is on the interval [0, 127] 

    Examples:
    "abc" >>> [-1, -1, -1]
    "a b" >>> [-1, 32, -1]
    "A B" >>> [-1, 32, -1]
    "ã b" >>> [227, 32, -1]
    "123" >>> [49, 50, 51]

    Parameter input: the string to remove non-alphabetic characters from
    Precondition: input is a string
    """
    assert isinstance(input,str), "input must be a string"

    index=0
    output=[]
    for s in range(len(input)):

        if (ord(input[s])<65
        or 90<ord(input[s])<97
        or ord(input[s])>122):

            output.append(ord(input[s]))

        else:
            output.append(-1)

    return output


######################################
######################################



def randChar():
    """
    Returns a randomized lowercase English character.

    The Unicode value of the character is always on the interval [97, 122].
    """
    randomizer=0
    randomizer=rand_randint(1, 26000000)
    randomizer=secr_randbelow(randomizer)
    randomizer=rand_randint(randomizer, randomizer*26000)
    if randomizer==0:
        randomizer=randomizer + rand_randint(1, 2600000)

    return chr(secr_randbelow(randomizer) % 26 + 97)


######################################
######################################



def readTextFromFile(filepath:str) -> str:
    """
    Returns the text from the .txt file at `filepath` as a string.

    If `filepath` is the empty string, the function reads from DEFAULT_INPUT_FILE, defined in this module.

    If no file at `filepath` exists, raises an OSError.
    If `filepath` doesn't end in ".txt" or is too short to contain the file extension, raises a FileTypeError (custom error).
    May raise a UnicodeDecodeError if the file at `filepath` contains unsupported characters.

    Parameter filepath: the name or absolute path of the file to read from
    Precondition: filepath is a string
    """
    assert isinstance(filepath, str), "file path must be a string"

    #Set file to read to be the default input file, or the given filepath if the path is not empty
    filepathToRead = DEFAULT_INPUT_FILENAME
    if len(filepath) > 0:
        filepathToRead = filepath
    
    #Ensure the file has the .txt extension
    if len(filepathToRead)<4 or not filepathToRead[-4:]==".txt":
        # print(repr(filepathToRead))
        raise FileTypeError("File must be a .txt file")

    #Take contents from the file and close it
    file = open(filepathToRead, "r")
    contents = file.read()
    file.close()

    return contents


######################################
######################################



def recombineSymbols(textPositions:list, symbolsPositions:list) -> str:
    """
    Returns `textPositions` as a string, with character values from `symbolsPositions` properly inserted inside.

    Undoes createBlocks(removeNonAlphas(...)) and getNonAlphaPositions(...).
    Note: If inputting a 2-D list for `textPositions`, flatten(...) must be called on the list.

    `textPositions` represents an output without non-alphabetic characters.
    Each index in `textPositions` contains a numerical value for a letter.
    Numerical values: a=0, b=1, c=2... z=25

    `symbolsPositions` contains characters at every index where there would normally be
    a non-alphabetic character in text, and a negative number otherwise.
    For recombineSymbols to be called properly, the number of negative numbers in `symbolsPositions` should equal the length of `textPositions`.

    Example:
    recombineSymbols([0,1,2, 3,4,5], [-1,-1,-1,32,-1,-1,-1]) = "abc def"

    Note: Any apostrophes in `symbolsPositions` should be ignored.

    Parameter textPositions: output with only alphabetic characters
    Precondition: textPositions is a 1-D array, all indices are ints on [0,25]

    Parameter symbolsPositions: contains indices where symbols would be in text
    Precondition: symbolsPositions is a 1-D array, all indices are ints. Amount of negative numbers should equal len(`textPositions`)
    """
    _recombineSymbolsAssert(textPositions, symbolsPositions)

    #Make defensive copies
    text=[]
    for a in textPositions:
        text.append(a)
    symbols=[]
    for a in symbolsPositions:
        symbols.append(a)

    output=""
    textIndex=0
    symbolsIndex=0
    effectiveTextLen=len(text)


    while symbolsIndex < effectiveTextLen:
        # print("Symbols index=" + str(symbolsIndex))
        # print("Text index=" + str(textIndex))
        # print('output=' + repr(output))
        # print()


        #If there's a symbol in the current index
        if symbols[symbolsIndex] >= 0:
            #If not an apostrophe
            if not(symbols[symbolsIndex]==39 or symbols[symbolsIndex]==96 or chr(symbols[symbolsIndex])=='’' or chr(symbols[symbolsIndex])=='`'):

                #add symbol to output
                output=output + chr(symbols[symbolsIndex])

                #empty the symbol so the same index doesn't get printed twice in a row
                symbols[symbolsIndex]=-127

            #increase effective length of text so all text symbols get printed
            #regardless of whether something was printed or not
            effectiveTextLen=effectiveTextLen+1

        #If there's not a symbol
        else:
            #add output text
            output=output + chr(text[textIndex]+97)
            textIndex=textIndex+1


        symbolsIndex=symbolsIndex+1


    #add the rest of the symbols
    while symbolsIndex < len(symbols):
        if symbols[symbolsIndex] >= 0:
            output=output + chr(symbols[symbolsIndex])

        symbolsIndex=symbolsIndex+1

    return output

######################################

def _recombineSymbolsAssert(textPos, symbolsPos):
    """
    Asserts preconditions for the reconbineSymbols function.
    
    Parameter textPos: text position list to check. 
    Precondition: none

    Parameter symbolPos: symbol position list to check.
    Precondition: none
    """
    assert type(textPos)==list, "Text positions must be a list"
    assert type(symbolsPos)==list, "Symbol positions must be a list"

    for v in range(len(textPos)):
        assert isinstance(textPos[v], int), "All text indices must be integers"

    symbolNegativeCount = 0
    for v in range(len(symbolsPos)):
        assert isinstance(symbolsPos[v], int), "All symbols indices must be integers"
        if symbolsPos[v] < 0:
            symbolNegativeCount += 1
    
    assert symbolNegativeCount == len(textPos), "Number of negative indices in the symbol positions list must equal the length of the text positions list"


######################################
######################################



def removeDiacritics(text:str) -> str:
    """
    Returns a copy of `text`, but with diacritics removed

    If a character in the text matches a character in outChars,
    it should be replaced by the corresponding character in inChars.
    outChars and inChars are lists of equal length defined in the function's body.

    Parameter text: the text to process
    Precondition: text is a string
    """
    assert isinstance(text,str), "input must be a string"

    outChars=["àáâãäå", "ç", "ð", "èéëêœæ", "ìíîï", "òóôõöø", "ǹńñň", "ß", "ùúûü", "ýÿ", "⁰₀", "¹₁", "²₂", "³₃", "⁴₄", "⁵₅", "⁶₆", "⁷₇", "⁸₈", "⁹₉"]
    inChars=['a', 'c', 'd', 'e', 'i', 'o', 'n', 's', 'u', 'y', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9']

    assert len(outChars) == len(inChars), "INTERNAL ERROR- outChars and inChars must be the same length"

    output=""
    inCharIndex=0
    charReplacement='#'

    #loop through each character of the text
    for textChar in text:

        #only check if character is alphabetical because the default replacement character is not alphabetical
        if textChar.isalnum():

            #loop through outChar strings
            for outString in outChars:

                #loop through individual letters. if match, set the replacement character to the ASCII replacement
                for c in outString:
                    if c.lower()==textChar.lower():
                        charReplacement=inChars[inCharIndex]

                #increase the selected index
                inCharIndex=inCharIndex+1


        #add character as itself if replacement character is default. otherwise, add the replacement
        if charReplacement=='#':
            output=output + textChar
        else:
            output=output + charReplacement

        #reset
        charReplacement='#'
        inCharIndex=0

    return output


######################################
######################################



def removeNonAlphas(input:str) -> str:
    """
    Returns a version of `input` with only lowercase alphabetic characters

    An alphabetic character is a letter in the English alphabet whose Unicode value is on the interval [0, 127].
    Any non-ASCII character or non-printing character is taken to be non-alphabetic.

    Parameter input: the text to separate out
    Precondition: input is a string
    """
    assert isinstance(input,str), "input must be a string"

    output=""
    for i in range(len(input)):
        if 65<=ord(input[i])<=90 or 97<=ord(input[i])<=122:
            output=output + input[i].lower()

    return output


######################################
######################################



def safeInput(message="") -> str:
    """
    Prints `message`, takes a string input from stdin, and returns the input.

    If any exception occurs during input, prints a message and calls sys.exit(0).
    Preferably prints special messages when a KeyboardInterrupt or EOFError is handled.

    Parameter message: message to print upon input
    Precondition: message is a string
    """
    assert isinstance(message,str), "input message must be a string"

    try:
        return input(message)
    
    except KeyboardInterrupt:
        print("")
        print("Program successfully exited")
        print("")
        sys.exit(0)

    except EOFError:
        print("Python successfully exited")
        print("")
        sys.exit(0)

    except:
        print("Error occurred during input")
        print("")
        sys.exit(0)


######################################
######################################



def setKeyBlockPositions(textLen:int) -> list:
    """
    Returns a list of ints representing the key block positions
    at position `textLen` in a given text

    The value at index 0 represents the position of the topmost key block. 
    Position BLOCK_COUNT-1 represents the position of the bottommost key block. 

    Parameter textLen: the index to calculate key block positions at. The first index in the text is index 0
    Precondition: textLen is a non-negative integer
    """
    assert isinstance(textLen,int), "ciphertext length must be an integer"
    assert textLen>=0, "length must be non-negative"

    #Handle special case
    if textLen < BLOCK_LENGTH:
        return [0]*BLOCK_COUNT

    #The process essentially converts a base-10 input to a base-BLOCK_SIZE output, then removes the rightmost digit

    result=[-1] * BLOCK_COUNT

    quotient=textLen-1
    decimalPortion=0
    # print(quotient)

    #eliminate block spill overs
    quotient=quotient % ((BLOCK_LENGTH ** BLOCK_COUNT) * BLOCK_COUNT)

    # print(quotient)

    #Divide quotient and save the portion right of the decimal
    decimalPortion = quotient/BLOCK_LENGTH - quotient//BLOCK_LENGTH
    #Divide quotient and keep the part left of the decimal
    quotient = quotient // BLOCK_LENGTH

    # print(quotient)
    # print(decimalPortion)

    for i in range(len(result)-1, -1, -1):
        #Divide quotient and save the portion right of the decimal
        decimalPortion = quotient/BLOCK_LENGTH - quotient//BLOCK_LENGTH
        #Divide quotient and keep the part left of the decimal
        quotient = quotient // BLOCK_LENGTH

        # print(quotient)
        # print(decimalPortion)

        #Convert the decimal portion to a digit and add to the result
        result[i] = int(round(decimalPortion*BLOCK_LENGTH))

        if quotient <= 0:
            break

    #Reverse the output
    result.reverse()
    #Remove any negative indices
    for i in range(len(result)):
        if result[i]<0:
            result[i]=0

    return result
