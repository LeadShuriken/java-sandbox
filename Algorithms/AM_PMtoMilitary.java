Scanner in = new Scanner(System.in);
String[] stringArr = in.nextLine().split(":");
in.close();

String timeZone = stringArr[2].substring(2, 4);
stringArr[2] = stringArr[2].substring(0, 2);

if(timeZone.equals("AM")){
    int a = Integer.parseInt(stringArr[0]);
    if(a >= 12){
        a = a - 12;
    }
    if(a < 0){
        a = (24 + a);
    } else if(a > 24){
        a = (a - 24);
    }
    stringArr[0] = Integer.toString(a);
    if(stringArr[0].length() == 1){
        stringArr[0] = "0" + stringArr[0];
    }
} else {
    int a = Integer.parseInt(stringArr[0]);

    if(a < 0){
        a = (24 + a);
    } else if(a >= 24){
        a = (a - 24);
    }
    stringArr[0] = Integer.toString(a);
    if(stringArr[0].length() == 1){
        stringArr[0] = "0" + stringArr[0];
    }
}

String resultString = stringArr[0] + ":" + stringArr[1] + ":" + stringArr[2];
System.out.println(resultString);