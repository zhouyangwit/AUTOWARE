package OilGasRecoveryCalculation;

import org.sqlite.core.DB;

public class MainCalculation {
    public static void main(String[] args) {

        //随便测试一下
        new MainCalculation().Calculation(300);
    }

    public void Calculation(int gasValue)
    {
        MainPipe(gasValue);
        liuliangji(gasValue);
        fengji(gasValue);
        xifuguan(gasValue,"25g/m3");

    }

    public int MainPipe(int gas)
    {
        double gas_speed=15.0;
        int dn=0;
        double dn_cal=0;
        dn_cal=Math.sqrt(gas*4.0/3.141592654/3600.0/gas_speed)*1000;
        System.out.println("计算出来的管径为："+dn_cal+"mm");
        if(dn_cal<=50)
        {
            dn=50;
        }else if(dn_cal<=80)
        {
            dn=80;
        }
        else if(dn_cal<=100)
        {
            dn=100;
        }
        else if(dn_cal<=150)
        {
            dn=150;
        }
        else if(dn_cal<=200)
        {
            dn=200;
        }
        else if(dn_cal<=250)
        {
            dn=250;
        }
        else if(dn_cal<=300)
        {
            dn=300;
        }
        else if(dn_cal<=350)
        {
            dn=350;
        }
        else{
            System.out.println("此项目管径计算值异常偏大，请核实数据是否合理！！！");
            dn=400;
        }
        System.out.println("流量："+gas+"m3/h，流速"+gas_speed+"m/s，则对应的管径为：DN"+dn);
        return dn;
    }

    public void xifuguan(int gas,String outputstanderd)
    {
        double tank_diameter=0;
        double tank_height=0;
        double gas_speed=0;
        double ratio=0;
        if(outputstanderd.equals("25g/m3"))
        {
            gas_speed=0.2;
        }else if(outputstanderd.equals("120mg/m3"))
        {
            gas_speed=0.05;
        }else{
            System.out.println("警告，排放标准存在问题，请检查，本次按默认25g/m3计算~~~");
            gas_speed=0.2;
        }
        tank_diameter=Math.sqrt(gas*4/3.141592654/3600/gas_speed)*1000;
        tank_diameter=Math.ceil(tank_diameter/100)*100;
        if(tank_diameter<600)
            tank_diameter=600;
        if(tank_diameter>=3000)
        {
            ratio=1.2;
        }else if(tank_diameter>=2000)
        {
            ratio=1.3;
        }else if(tank_diameter>=1000)
        {
            ratio=1.4;
        }else{
            ratio=1.5;
        }
        tank_height=Math.ceil(tank_diameter*ratio/100)*100;
        if(tank_height<1600)
            tank_height=1600;
        double tank_volume=3.141592654/4*tank_diameter*tank_diameter*tank_height/1000/1000/1000;
        double carbon=tank_volume*450;
        carbon=Math.ceil(carbon);
        System.out.println("排放标准"+outputstanderd+"，吸附罐单罐尺寸："+tank_diameter+" mm x "+tank_height+" mm ，单罐活性炭消耗："+carbon+"kg，活性炭总消耗："+carbon*2/1000+"t");
    }

    public void liuliangji(int gas)
    {
        double min=Math.ceil(gas*0.3/10)*10;
        double max=Math.ceil(gas*1.1/10)*10;
        System.out.println("流量计取值范围："+min+"m3/h~"+max+"m3/h");
    }

    public void fengji(int gas)
    {
        double min,max;
        if(gas<500)
        {
            min=Math.ceil(gas*0.3/10)*10;
            max=Math.ceil(gas*1.1/10)*10;
            System.out.println("风机数量：1台，流量范围："+min+"m3/h~"+max+"m3/h");
        }else{
            min=Math.ceil(gas*0.3/2/10)*10;
            max=Math.ceil(gas*1.1/2/10)*10;
            System.out.println("风机数量：2台，单台风机流量范围："+min+"m3/h~"+max+"m3/h");
        }
    }

}
