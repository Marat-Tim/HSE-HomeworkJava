public class Main {
    public static void main(String[] args) {
        PhoneFactory phoneFactory = new IphoneFactory();
        phoneFactory.sellPhones();
        System.out.println("!Начались санкции!");
        phoneFactory = new XiaomiFactory();
        phoneFactory.sellPhones();
    }
}