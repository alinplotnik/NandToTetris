// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

//// Replace this comment with your code.

// Multiplies R0 and R1 and stores the result in R2
// Uses repetitive addition algorithm
@R2      
M=0        // R2 = 0
@R1
D=M        //D=[R1]
@STOP      //if [R1]=0 stop 
D;JEQ
@R0
D=M        //D=[R0]
@STOP      //if [R0]=0 stop 
D;JEQ
@i         
M=1       // i=0
(LOOP)
@i             
D=M              // D=[i]
@R0
D=D-M            // D=[i]-[R0]
@STOP             
D;JGT           //if [i] > [R0] stop the loop
@R2
D=M             // D=[R2]
@R1
D=D+M            // D= [R2] + [R1]    
@R2
M=D              // [R2]= [R2] + [R1]
@i
M=M+1             //[i]= [i] + 1
@LOOP
D;JGT            // while D>0 
(STOP)
(END)
@END
0;JMP