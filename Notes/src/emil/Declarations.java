package emil;

enum curPanelType{
    note(0),
    reminder(1),
    archive(2),
    basket(3);
    final int value;
    curPanelType(int value){
        this.value = value;
    }
}
