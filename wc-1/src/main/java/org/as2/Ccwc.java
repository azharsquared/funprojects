package org.as2;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Callable;

@Command(name = "ccwc", mixinStandardHelpOptions = true, version = "ccwc 1.0",
description = "counts the number for lines, words, characters ")
public class Ccwc implements Callable<Result>{

    public static void main(String[] args) {
        var cmd = new CommandLine(new Ccwc());
        cmd.execute(args);
        Result result = cmd.getExecutionResult();
        if (result != null) {
            System.out.println(result);
        }
    }
    public Ccwc(){

    }


    @Option(names = {"-c"}, description = "-c for counting characters")
    private boolean switchCharacters;

    @Option(names = {"-l"}, description = "-l for counting lines")
    private boolean switchLines; 

    @Option(names = {"-w"}, description = "-l for counting words")
    private boolean switchWords;

    @Parameters(index = "0", description = "The file to calculate for.")
    private File file;

    @Override
    public Result call() throws Exception {
        if (!this.file.exists()) {
            throw new FileNotFoundException("File "+this.file.getAbsolutePath()+" does not exist");
        }
        return new Wc(this.file).count(this.switchLines, this.switchWords, this.switchCharacters);
    }

}