public class ExtraAsymptotics {

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                long arg = Long.parseLong(args[0]);
                if (arg < 0) {
                    useage();
                    return;
                }

                Stopwatch timer = new Stopwatch();
                mysteryFunction(arg);
                System.out.println(timer.elapsedTime() + " seconds elapsed");
            } catch (NumberFormatException e) {
                useage();
            }
        } else {
            useage();
        }
    }

    public static void mysteryFunction(long n) {
        for (long i = 0; i < n; i++) {
            long j = i * i;
            while (j <= n) {
                j += 1;
            }
        }
    }

    public static void useage() {
        System.out.println("To run ExtraAsymptotics loop, use the command java ExtraAsymptotics n, where n is the input value.");
    }
}