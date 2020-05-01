var hContrast = document.getElementById("contrast-eye");
var header = document.getElementById("header");
var subheader = document.getElementById("subheader");
var addResourceButton = document.getElementById("addResource");
var mainLayout = document.getElementsByTagName("body")[0];
var footer = document.getElementsByTagName("footer")[0];
var state = localStorage.getItem("contrastMode");

if(state === null){
    setContrastModeOff();
}
else if(state === "0"){
    setContrastModeOff();
}
else{
    setContrastModeOn();
}

hContrast.onmouseover = function(){
    if(state === 0){
        addContrastEye();
    }
    else{
        removeContrastEye();
    }
};

hContrast.onmouseleave = function(){
    if(state === 0){
        removeContrastEye();
    }
    else{
        addContrastEye();
    }
};

hContrast.addEventListener("click", function(){
    if(state === 0){
        setContrastModeOn();
    }else if(state === 1){
        setContrastModeOff();
    }
});

function setContrastModeOn(){
    state = 1;
    localStorage.setItem("contrastMode", "1");
    subheader.classList.add("subheader-contrast");
    header.classList.add("header-contrast");
    addResourceButton.classList.add("add-contrast");
    mainLayout.classList.add("bodyBackground-contrast");
    footer.classList.add("footer-contrast");
    addContrastEye();
}

function setContrastModeOff(){
    state = 0;
    localStorage.setItem("contrastMode", "0");
    subheader.classList.remove("subheader-contrast");
    header.classList.remove("header-contrast");
    addResourceButton.classList.remove("add-contrast");
    mainLayout.classList.remove("bodyBackground-contrast");
    footer.classList.remove("footer-contrast");
    removeContrastEye();
}

function removeContrastEye(){
    hContrast.classList.add("material-icons-outlined");
    hContrast.classList.remove("material-icons");
}

function addContrastEye(){
    hContrast.classList.remove("material-icons-outlined");
    hContrast.classList.add("material-icons");
}

function searchbookmarks() {
    var input, filter, bookmarks, title, i;
    var titles = [];
    input = document.getElementById('search');
    console.log(input);
    filter = input.value.toUpperCase();
    bookmarks = document.getElementsByTagName('article');
    console.log(bookmarks);
    for (i = 0; i < bookmarks.length; i++) {
        titles.push(bookmarks[i].dataset.bookmarktitle);
    }
    console.log(titles);
    for (i = 0; i < bookmarks.length; i++) {
        title = bookmarks[i].dataset.bookmarktitle;
        if (title.toUpperCase().indexOf(filter) > -1) {
            bookmarks[i].style.display = "";
        } else {
            bookmarks[i].style.display = "none";
        }

    }
}