import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String listname = "listname";
        long line_numbers = countLineBufferedReader(listname);
        int thread_calc = 43;

        long start = 0;
        long end = (int) Math.floor(line_numbers/thread_calc);


        for (int i=0; i<thread_calc ; i++){
            making_thread th = new making_thread(start,end);
            th.start();
            start = end;
            end = end + (int) Math.floor(line_numbers/thread_calc);
        }

        if(start < line_numbers) {
            making_thread th = new making_thread(start, line_numbers);
            th.start();
        }

    }

    private static void FileCheking(String listname, long start , long end){
        String separated_line;
        String word1 = "2=pixar";
        String word2 = "6=Ubuntu OS";
        String[] split1 = word1.split("=");
        String[] split2 = word2.split("=");


        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(listname))) {
            for (int i = 0; i < start; i++)
                bufferedReader.readLine();
            for (long i = start; i < end; i++){
                separated_line = bufferedReader.readLine();
                String[] model = separated_line.split(",");
                if(model.length >= Integer.valueOf(split1[0]) && model.length >=Integer.valueOf(split2[0]))
                {
                    if(model[Integer.valueOf(split1[0])-1].equals(split1[1]) && model[Integer.valueOf(split2[0])-1].equals(split2[1]))
                    {
                        System.out.println(separated_line);
                    }
                }
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public static long countLineBufferedReader(String listname) {

        long line_numbers = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(listname))) {
            while (bufferedReader.readLine() != null) line_numbers++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line_numbers;
    }

    public static class making_thread extends Thread {

        long start;
        long end;

        public making_thread (long start , long end ) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            String listname = "listname";
            FileCheking(listname, start, end);
        }
    }
}
