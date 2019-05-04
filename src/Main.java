import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {

    static int pciCounter = 1;
    static String[] nodeNames;
    static ArrayList<ArrayList<Integer>> pcis = new ArrayList();

    public static void nodeNamePlaceholder(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("NodeNames"));
            nodeNames = reader.readLine().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean[] disjuncture(boolean[] a, boolean[] b){
        boolean[] res = new boolean[a.length];
        for (int i = 0 ; i < a.length ; i++) {
            res[i] = Boolean.logicalOr(a[i], b[i]);
        }
        return res;
    }

    public static int zerosSearch(boolean a[],int start){
        int res = -1;
        for (int i = start; i < a.length ; i++)
            if( !a[i] )
                return i;
        return res;
    }

    public  static void printFormatted(ArrayList<Integer> list){
        System.out.print("Ψ"+pciCounter+++":{");
        list.stream().forEach((a) -> System.out.print( nodeNames[a]+" "));
        System.out.print("}");
        System.out.println();
    }

    static void makingPciMatrix(ArrayList<ArrayList<Integer>> a){
        int matrix[][] = new int[a.size()][a.size()];
        for (int i = 0 ; i < a.size()-1 ; i++) {
            System.out.print(i+1+"\t");
            for(int k = 0 ; k < i ; k++)
                System.out.print("\t");
            for (int j = i+1 ; j < a.size() ; j++){
                ArrayList intersection = new ArrayList();
                intersection.addAll(a.get(i));
                intersection.addAll(a.get(j));
                System.out.print(intersection.stream().distinct().count()+"\t");

            }
            System.out.println();
        }
    }

    private static void petuhovAlgorithm() throws IOException {
        ArrayList<Integer> zeroElements = new ArrayList();
        BufferedReader reader = new BufferedReader(new FileReader("matrix"));
        nodeNamePlaceholder();
        int size = reader.readLine().length();
        boolean matrix[][] = new boolean[size][size];
        reader.close();
        reader = new BufferedReader(new FileReader("matrix"));
        for(int i = 0 ; i < size ; i++){
            char a[] = reader.readLine().toCharArray();
            for(int j = 0 ; j < size ; j++){
                if(a[j] == '1')
                    matrix[i][j] = true;
            }
        }
        for (int i = 0 ; i < size-1 ; i++) {
            for (int j = i+1 ; j < size ; j++)
                if( !matrix[i][j] )
                    zeroElements.add(j);
            boolean firstLine[] = disjuncture(matrix[i],matrix[zeroElements.get(0)]);

            for (int j = 1 ; j < zeroElements.size() ; j++) {
                boolean tmp[] = disjuncture(firstLine,matrix[zeroElements.get(j)]);
                ArrayList pci = new ArrayList();
                pci.add(i);
                pci.add(zeroElements.get(0));
                pci.add(zeroElements.get(j));
                //из tmp нужно найти нулевые элементы чей индекс старше j
                int zerosIndex = 0;
                if(zerosSearch(tmp,0) == -1) { // если tmp все единицы
                    printFormatted(pci);
                    pci.clear();
                    continue;
                }
                while ((zerosIndex = zerosSearch(tmp,zeroElements.get(j))) > 0){
                    // пытаемся заполнить tmp единицами с помощью дизъюнкции
                    tmp = disjuncture(tmp,matrix[zerosIndex]);
                    pci.add(zerosIndex);
                    if(zerosSearch(tmp,0) == -1) // если tmp все единицы
                    {
                        printFormatted(pci);
                        pcis.add((ArrayList<Integer>) pci.clone());
                        pci.clear();
                        break;
                    }
                }
                pci.clear();


            }
            zeroElements.clear();
        }

    }

    public static void main(String[] args) throws Exception {
        petuhovAlgorithm();
        makingPciMatrix(pcis);
        pciCounter = 1;
        ArrayList max1 = (ArrayList) pcis.get(5).clone();
        ArrayList max2 = (ArrayList) pcis.get(16).clone();
        pcis.remove(16);
        pcis.remove(5);
        for (int i = 0 ; i < pcis.size() ; i++){
            pcis.get(i).removeAll(max1);
            pcis.get(i).removeAll(max2);
        }
        pciCounter = 1;
        pcis = pcis.stream().distinct().filter(v -> v.size() > 0).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0 ; i < pcis.size() ; i++){
            printFormatted(pcis.get(i));
        }
        pciCounter = 1;
        makingPciMatrix(pcis);
        pciCounter = 1;
        max1 = (ArrayList) pcis.get(0).clone();
        max2 = (ArrayList) pcis.get(7).clone();
        pcis.remove(7);
        pcis.remove(0);
        for (int i = 0 ; i < pcis.size() ; i++){
            pcis.get(i).removeAll(max1);
            pcis.get(i).removeAll(max2);
        }
        pciCounter = 1;
        pcis = pcis.stream().distinct().filter(v -> v.size() > 0).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0 ; i < pcis.size() ; i++){
            printFormatted(pcis.get(i));
        }
        pciCounter = 1;
        makingPciMatrix(pcis);

    }
}
