/**
 * 老鼠喝毒药问题
 * 有1024瓶药，其中有一瓶是毒药，请找出毒药
 * 用老鼠试毒，毒药不是一喝即死的
 * 老鼠的数量自定义
 */

public class MouseAndBane {
    private int[] banes = new int[1024];

    private void init() {
        int index = (int) Math.floor(Math.random() * 1024);
        banes[index] = 1;
    }

    /** 线性查找，用一只老鼠测 */
    public int linearSearch() {
        Mouse mouse = new Mouse();
        int index = 0;
        for (int i : banes) {
            mouse.eat(i);
            if (mouse.isDie()) return index;
            index++;
        }
        return -1;
    }

    /** 二分查找，每次用两只老鼠各喝一半数量的药，总会有一只老鼠死亡 */
    public int binarySearch() throws InterruptedException {
        int start = 0;
        int mid = banes.length / 2;
        Mouse before = new Mouse();
        Mouse after = new Mouse();
        return binarySearch(start, mid, before, after);
    }

    private int binarySearch(int start, int mid, Mouse before, Mouse after) throws InterruptedException {

        if (start >= mid) return start;

        Thread thread0 = new Thread(() -> {
            int i = 0;
            while (i + start < mid) {
                before.eat(banes[i + start]);
                i++;
            }
        });
        Thread thread1 = new Thread(() -> {
            int i = 0;
            while (i + start < mid) {
                after.eat(banes[i + mid]);
                i++;
            }
        });
        thread0.start();
        thread1.start();
        thread0.join();
        thread1.join();
        if (before.isDie()) {
            return binarySearch(start, (start + mid) / 2, after, new Mouse());
        } else {
            return binarySearch(mid, (mid + banes.length) / 2, before, new Mouse());
        }
    }

    /** 编码法，2^10=1024 即可以用10位2进制来表示每一瓶毒药，因此准备10只老鼠，将1024瓶毒药的编号转为2进制
     * 2进制数位为1对应老鼠喝药，最后死亡的老鼠表示一个2进制数
     */
    public int encodeSearch() {
        Mouse[] mouses = initMouses(10);
        int i = 0;
        while (i < 1024) {
            String[] binaryArray = intToBinaryArray(i);
            binaryArray = fillBinaryArray(binaryArray);
            for(int j = 0; j < 10; j++) {
                if(binaryArray[j].equals("1")) mouses[j].eat(banes[i]);
            }
            i++;
        }
        return getBena(mouses);
    }

    public int getBena(Mouse[] mouses) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            sb.append(mouses[i]);
        }
        return Integer.parseInt(sb.toString(), 2);
    }

    public Mouse[] initMouses(int len) {
        Mouse[] mouses = new Mouse[len];
        int i = 0;
        while (i < len) {
            mouses[i++] = new Mouse();
        }
        return mouses;
    }

    public String[] intToBinaryArray(int i) {
        return Integer.toBinaryString(i).split("");
    }

    public String[] fillBinaryArray(String[] binaryArray) {
        while(binaryArray.length < 10) {
            binaryArray = (String[])ArrayUtil.unshift(binaryArray, "0");
        }
        return binaryArray;
    }

    public static void main(String[] args) throws InterruptedException {
        MouseAndBane mab = new MouseAndBane();
        mab.init();
        int index = mab.linearSearch();
        System.out.println("线性查找结果: 毒药在第" + index + "瓶");
        index = mab.binarySearch();
        System.out.println("二分查找结果: 毒药在第" + index + "瓶");
        index = mab.encodeSearch();
        System.out.println("编码查找结果: 毒药在第" + index + "瓶");
    }
}

class Mouse {
    private boolean isDie = false;

    public void eat(int bane) {
        if (bane == 1) die();
    }

    public void die() {
        isDie = true;
    }

    public boolean isDie() {
        return isDie;
    }

    @Override
    public String toString() {
        return isDie ? "1" : "0";
    }
}
