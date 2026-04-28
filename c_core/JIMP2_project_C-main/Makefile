CC     = gcc
CFLAGS = -Wall -Wextra -std=c11
SRC    = ./src/*.c
BIN    = ./bin/graph

all: build

build:
	$(CC) $(CFLAGS) $(SRC) -o $(BIN) -lm

run-fr:
	$(BIN) ./inp.txt ./out.txt fr text

run-tutte:
	$(BIN) ./inp.txt ./out.txt tutte text

run-fr-bin:
	$(BIN) ./inp.txt ./out.bin fr bin

run-tutte-bin:
	$(BIN) ./inp.txt ./out.bin tutte bin

clean:
	rm -f $(BIN) ./out.txt ./out.bin
