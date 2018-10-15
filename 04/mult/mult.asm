// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

// COUNT = RAM[1]
// IF COUNT > 0
// 	RAM[2] = RAM[2] + RAM[0]
// 	COUNT = COUNT+1

	@COUNT
	M = 1

	@R2
	M = 0

	@R0
	D = M

	@HALT
	D;JEQ

	@R1
	D = M

	@HALT
	D;JEQ

(LOOP)
	
	@R0
	D = M

	@R2
	M = M + D

	@COUNT
	D = M + 1
	M = D

	@R1
	D = D - M
	@LOOP
	D;JLE

	@HALT
(HALT)
	0;JMP