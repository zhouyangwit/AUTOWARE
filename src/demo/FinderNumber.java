package demo;
//瞎几把写的程序，用于寻找勾股数
public class FinderNumber {
    public static void main(String[] args) {
        new FinderNumber();
    }
    FinderNumber(){
        getStart(10000);
    //    common_divisor(333,666,999);
    }

    int prime_num=0;
    public void getStart(int MAX_NUM)
    {
        int x,y,z;
        for(z=5;z<=MAX_NUM;z++)
        {
            for(y=z;y>0;y--)
            {
                for(x=y;x>0;x--)
                {
                    isPrime(x,y,z);
                }
            }
        }
    }

    public void isPrime(int x,int y,int z)
    {
        if(x*x+y*y==z*z) {
            if (!no_duplicate(x,y,z)) {
                prime_num++;
                System.out.println(prime_num + ":\t" + x + "\t" + y + "\t" + z);
            }
        }
    }

    public boolean no_duplicate(int x,int y,int z)
    {
        boolean isDumplicate=false;
        for(int i = x; i>=2; i--)
        {
            if((x%i==0)&&(y%i==0)&&(z%i==0))
            {
                isDumplicate=true;
                break;
            }
        }
        return isDumplicate;
    }

    public void common_divisor(int x,int y,int z)
    {
        for(int i=2;i<=x;i++)
        {
            if((x%i==0)&&(y%i==0)&&(z%i==0))
            {
                x=x/i;
                y=y/i;
                z=z/i;
                System.out.println(i+"\t"+x+"\t"+y+"\t"+z);
                common_divisor(x,y,z);
                break;
            }
        }
    }

}
