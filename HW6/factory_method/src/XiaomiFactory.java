public class XiaomiFactory extends PhoneFactory {
    @Override
    protected Phone createPhone() {
        return new Xiaomi();
    }
}
