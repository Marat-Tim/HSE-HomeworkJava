public abstract class PhoneFactory {
    protected abstract Phone createPhone();

    public void sellPhones() {
        for (int i = 0; i < 10; i++) {
            Phone phone = createPhone();
            phone.sell();
        }
    }
}
