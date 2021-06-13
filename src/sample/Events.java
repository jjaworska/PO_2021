package sample;

public class Events {
    public static void drying(Board b)
    {
        for (int i = 0; i < b.height; i++)
            for(int j = 0; j < b.width; j++) {
                int c=0;
                if(i==0 || b.fields[i-1][j].isWater) c++;
                if(j==0 || b.fields[i][j-1].isWater) c++;
                if(i==b.height-1 || b.fields[i+1][j].isWater) c++;
                if(j==b.width-1 || b.fields[i][j+1].isWater) c++;
                if(c<=2) b.fields[i][j].drying=true;
            }
        for (int i = 0; i < b.height; i++)
            for(int j = 0; j < b.width; j++) {
                if(b.fields[i][j].drying){
                    b.fields[i][j].drying=false;
                    b.fields[i][j].isWater=false;
                }
            }
    }
    public static void cropFailure(Board b)
    {
        for (int i = 0; i < b.height; i++)
            for(int j = 0; j < b.width; j++) {
                b.fields[i][j].food=false;
            }
    }
}
