use gtk::prelude::*;

fn main() {
    if gtk::init().is_err() {
        println!("Failed to initialize GTK");
        return;
    }

    let glade_src = include_str!("window.glade");
    let builder = gtk::Builder::new_from_string(glade_src);

    let window: gtk::Window = builder.get_object("window1").unwrap();
    let button: gtk::Button = builder.get_object("button1").unwrap();
    let dialog: gtk::MessageDialog = builder.get_object("messagedialog1").unwrap();

    button.connect_clicked(move |_| {
        dialog.run();
        dialog.hide();
    });

    window.show_all();

    gtk::main();
}
