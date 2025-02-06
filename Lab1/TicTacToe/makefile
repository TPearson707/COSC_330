JAVAC = javac
SRC_DIR = src
BIN_DIR = bin
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

.PHONY: all clean run

all: $(SOURCES)
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)

run: all
	@java -cp $(BIN_DIR) Main

clean:
	rm -rf $(BIN_DIR)/*
