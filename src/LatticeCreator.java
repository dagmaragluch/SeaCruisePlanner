public class LatticeCreator {

    double latA = 53.94;    //A - Swinoujście
    double lngA = 14.28;
    double latB = 55.09;    //B - Borholm
    double lngB = 14.69;    // odl. - ok. 135 km

    double a = 0.2;     // dł boku trójkąta równobocznego; w stopniach geogr. !
    double h = a / 2 * Math.sqrt(3);  // wysokość w trójkącie równobocznym; też w stopniach geogr. !
    int k = 3;      //szerokość kraty jako ilość punktów po każedej ze stron odcinka AB

    double firstConVectorAB = latB - latA;
    double secondConVectorAB = lngB - lngA;


    double distanceAB = Math.sqrt((latB - latA) * (latB - latA) + (lngB - lngA) * (lngB - lngA));
    double mnoznik = distanceAB / a;


    public void createLattice() {
        int l = 1;  //licznik
        double tempLat = latA;
        double tempLng = lngA;
        double tempMnoznik;

//        for (int i = -k; i <= k; i++) {
//            tempLat =
//        }


        do {
            tempMnoznik = mnoznik * l;
            tempLat = firstConVectorAB * tempMnoznik;
            tempLng = secondConVectorAB * tempMnoznik;
            l++;


        } while (tempMnoznik <= 1.0);


    }


}
