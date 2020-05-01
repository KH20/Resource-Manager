var share = document.querySelectorAll(".share");

for(var i=0;i<share.length;i++){
    share[i].addEventListener("click",function(){
        var bookmarkData = JSON.parse(this.dataset.bookmark);
        var url = (bookmarkData.url.valueOf());
        copyToClipboard(url);
        alert(bookmarkData.title.valueOf() + " url copied to clipboard");
    });
}

function copyToClipboard(text) {
    var dummy = document.createElement("textarea");
    document.body.appendChild(dummy);
    dummy.value = text;
    dummy.select();
    document.execCommand("copy");
    document.body.removeChild(dummy);
}

var upvote1 = document.querySelectorAll(".upvote1");
var downvote1 = document.querySelectorAll(".downvote1");
var favourite1 = document.querySelectorAll(".favourite1");


for(var i=0;i<upvote1.length;i++){
    upvote1[i].addEventListener("click", function(){
        var upvotes = JSON.parse(this.dataset.bookmark);
        var downvote = document.getElementById("downvote" + upvotes.id);

        if(upvotes.liked.valueOf() === false && upvotes.disliked.valueOf() === true){
            this.classList.add("upvote-clicked");
            downvote.classList.remove("downvote-clicked");
            upvotes.likes+=2;
            upvotes.liked = true;
            upvotes.disliked = false;
            document.getElementById("voteNumber1_" + upvotes.id).innerHTML = upvotes.likes;
        }
        else if(upvotes.liked.valueOf() === false){
            this.classList.add("upvote-clicked");
            downvote.classList.remove("downvote-clicked");
            upvotes.likes+=1;
            upvotes.liked = true;
            document.getElementById("voteNumber1_" + upvotes.id).innerHTML = upvotes.likes;
        }
        else if(upvotes.liked.valueOf() === true){
            this.classList.remove("upvote-clicked");
            upvotes.likes-=1;
            upvotes.liked = false;
            document.getElementById("voteNumber1_" + upvotes.id).innerHTML = upvotes.likes;
        }
    });
}

for(var i=0; i<downvote1.length;i++){
    downvote1[i].addEventListener("click", function(){
        var votes = JSON.parse(this.dataset.bookmark);
        var upvote = document.getElementById("upvote" + votes.id);

        if(votes.liked.valueOf() === true && votes.disliked.valueOf() === false){
            this.classList.add("downvote-clicked");
            upvote.classList.remove("upvote-clicked");
            votes.likes-=2;
            votes.liked = false;
            votes.disliked = true;
            document.getElementById("voteNumber1_" + votes.id).innerHTML = votes.likes;
        }
        else if(votes.disliked.valueOf() === false){
            this.classList.add("downvote-clicked");
            upvote.classList.remove("upvote-clicked");
            votes.likes-=1;
            votes.disliked = true;
            document.getElementById("voteNumber1_" + votes.id).innerHTML = votes.likes;
        }
        else if(votes.disliked.valueOf() === true){
            this.classList.remove("downvote-clicked");
            votes.likes+=1;
            votes.dislikes = false;
            document.getElementById("voteNumber1_" + votes.id).innerHTML = votes.likes;
        }
    });
}

for(var i =0;i<favourite1.length;i++){
    favourite1[i].addEventListener("click", function(){
        var bookmarks = JSON.parse(this.dataset.bookmark);
        console.log(this);
        if(bookmarks.favourited.valueOf() === true){
            this.classList.remove("favourite-clicked");
            bookmarks.favourited = false;
            this.innerHTML = "<span class='mdi mdi-heart-outline'></span>";
        }
        else if(bookmarks.favourited.valueOf() === false){
            this.classList.add("favourite-clicked");
            bookmarks.favourited = true;
            this.innerHTML = "<span class='mdi mdi-heart'></span>";
        }

    });

}