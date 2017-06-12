package com.asdf.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiyu on 17-6-12.
 */
public class ProcessTest {
    public static void main(String[] args) {
        ProcessBuilder processBuilder = new ProcessBuilder("ls");
        Process proc;
        List<String> errorOutput = new ArrayList<String>();
        List<String> stdOutput = new ArrayList<String>();
        try {
            System.out.println("Before execute");
            proc = processBuilder.start();
            grabProcessOutput(proc, errorOutput, stdOutput, true);
            System.out.println("After execute");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int grabProcessOutput(final Process process, final List<String> errorOutput,
                                     final List<String> stdOutput, boolean waitForReaders) {

        if (errorOutput != null && stdOutput != null) {
            Thread t1 = new Thread(":stderr reader") {
                @Override
                public void run() {
                    InputStreamReader is = new InputStreamReader(process.getErrorStream(), Charset.forName("UTF-8"));
                    BufferedReader errReader = new BufferedReader(is);

                    try {
                        while (true) {
                            String line = errReader.readLine();
                            if (line != null) {
                                System.out.println("[ERR] " + line);
                                errorOutput.add(line);
                            } else {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            errReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            Thread t2 = new Thread(":stdout reader") {
                @Override
                public void run() {
                    InputStreamReader is = new InputStreamReader(process.getInputStream());
                    BufferedReader outReader = new BufferedReader(is);

                    try {
                        while (true) {
                            String line = outReader.readLine();
                            if (line != null) {
                                System.out.println("[OUT] " + line);
                                stdOutput.add(line);
                            } else {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            outReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            t1.start();
            t2.start();

            if (waitForReaders) {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }
}
