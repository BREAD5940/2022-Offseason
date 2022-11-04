package frc.robot.autonomus.routines;

public class Jude {
    private String rhymes[] = new String[10];

    public void printList() {
        int i = 0;
        while (i < 10) {
            System.out.println(rhymes[i]);
            i++;
        }
    }

    public void updateRhyme(int index, String item){
        if (index < 10){
            rhymes[index] = item;
        }
    }
}
