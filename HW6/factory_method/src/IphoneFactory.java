public class IphoneFactory extends PhoneFactory {
    @Override
    protected Phone createPhone() {
        return new Iphone();
    }
}
