function showHideTagBox(ayatID){
    if(username == ''){
        alert(userNotConnected);
        return;
    }
    $("#tag_"+ayatID).toggle();
}

function showHideArabAyat(ayatID){
    $("#arab_"+ayatID).toggle();
}

function toogleArabic(){

    var self = this, doMask = function() {
        $("body").mask("");
    };
    $.queueTimer.add(doMask, this);

    $('div.ayatArabic').each(function() {
        var self = this, doToggle = function() {
            $(self).toggle();
        };
        $.queueTimer.add(doToggle, this);
    });

    self = this, doUnMask = function() {
        $("body").unmask();
    };
    $.queueTimer.add(doUnMask, this);



}

function toogleOptions(){

    var self = this, doMask = function() {
        $("body").mask("");
    };
    $.queueTimer.add(doMask, this);
    
    $('div.options').each(function() {
        var self = this, doToggle = function() {
            if($(self).hasClass("textarea")){
                $(self).hide();
            }else{
                $(self).toggle();
            }
            
        };
        $.queueTimer.add(doToggle, this);
    });

    self = this, doUnMask = function() {
        $("body").unmask();
    };
    $.queueTimer.add(doUnMask, this);

    
     
}

function showComment(ayatID,load){
    
    if($("#ayatPublicComment_"+ayatID).text().trim() == "" && load == "yes"){
        $.post(publicCommentLoadURL, {
            ayatID: ayatID
        },
        function(data){
            $("#ayatPublicComment_"+ayatID).html(data.content);
            $("#ayatPublicCommentBox_"+ayatID).slideToggle("slow");
        });
    }else{
        $("#ayatPublicCommentBox_"+ayatID).slideToggle("slow");
    }
    
}

function showPrivateCommentBox(id,load){
    if(load == "yes"){
        $("#privateComment_"+id).html('<textarea id="privateCommentArea_'+id+'" class="privateCommentArea_'+id+'" cols="80" rows="8"></textarea>');

        $.post(privateCommentLoadURL, {
            ayatId: id
        },
        function(data){
            $("#privateCommentArea_"+id).html(data.content);
        });
    }

    tinyMCE.init({
        
        mode : "specific_textareas",
        editor_selector : "privateCommentArea_"+id,

        language : "fr",
        height: "400",
        width: "900",
        plugins : "pagebreak,style,layer,table,save,advhr,inlinepopups,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
        theme : "advanced",
        theme_advanced_resizing : false,
        theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,|,\n\
justifyleft,justifycenter,justifyright,justifyfull,|,fontsizeselect,hr,removeformat,|,\n\
cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,|,\n\
undo,redo,|,link,unlink,cleanup,|,forecolor,backcolor",
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 :"",
        theme_advanced_buttons4 :"",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        
        cleanup: false,
        convert_fonts_to_spans : false,
        inline_styles: false,
        fix_table_elements : true,
        fix_list_elements : true,
        relative_urls : false,
        convert_urls : false,
        extended_valid_elements :
        "a[href|rel|rev|target|title|type]," +
    "b[],"+
    "blink[],"+
    "blockquote[align|cite|clear|height|type|width],"+
    "br[clear],"+
    "caption[align|height|valign|width],"+
    "center[align|height|width],"+
    "col[align|bgcolor|char|charoff|span|valign|width],"+
    "colgroup[align|bgcolor|char|charoff|span|valign|width],"+
    "comment[],"+
    "em[],"+
    "font[color|face|font-weight|point-size|size],"+
    "h1[align|clear|height|width],"+
    "h2[align|clear|height|width],"+
    "h3[align|clear|height|width],"+
    "h4[align|clear|height|width],"+
    "h5[align|clear|height|width],"+
    "h6[align|clear|height|width],"+
    "hr[align|clear|color|noshade|size|width],"+
    "img[align|alt|border|height|hspace|src|vspace|width],"+
    "li[align|clear|height|type|value|width],"+
    "marquee[behavior|bgcolor|direction|height|hspace|loop|scrollamount|scrolldelay|vspace|width],"+
    "ol[align|clear|height|start|type|width],"+
    "p[align|clear|height|width],"+
    "pre[clear|width|wrap],"+
    "s[],"+
    "small[],"+
    "span[align],"+
    "strike[],"+
    "strong[],"+
    "sub[],"+
    "sup[],"+
    "table[align|background|bgcolor|border|bordercolor|bordercolordark|bordercolorlight|"+
    "bottompadding|cellpadding|cellspacing|clear|cols|height|hspace|leftpadding|"+
    "rightpadding|rules|summary|toppadding|vspace|width],"+
    "tbody[align|bgcolor|char|charoff|valign],"+
    "td[abbr|align|axis|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|headers|"+
    "height|nowrap|rowspan|scope|valign|width],"+
    "tfoot[align|bgcolor|char|charoff|valign],"+
    "th[abbr|align|axis|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|headers|"+
    "height|nowrap|rowspan|scope|valign|width],"+
    "thead[align|bgcolor|char|charoff|valign],"+
    "tr[align|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|"+
    "height|nowrap|valign],"+
    "tt[],"+
    "u[],"+
    "ul[align|clear|height|start|type|width]"


    });
}

function showPublicCommentBox(id){

    $("#publicComment_"+id).html('<textarea id="publicCommentArea_'+id+'" class="publicCommentArea_'+id+'" cols="80" rows="8"></textarea>');


    tinyMCE.init({

        mode : "specific_textareas",
        editor_selector : "publicCommentArea_"+id,

        language : "fr",
        height: "400",
        width: "900",
        plugins : "pagebreak,style,layer,table,save,advhr,inlinepopups,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
        theme : "advanced",
        theme_advanced_resizing : false,
        theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,|,\n\
justifyleft,justifycenter,justifyright,justifyfull,|,fontsizeselect,hr,removeformat,|,\n\
cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,|,\n\
undo,redo,|,link,unlink,cleanup,|,forecolor,backcolor",

        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 :"",
        theme_advanced_buttons4 :"",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        cleanup: false,
        convert_fonts_to_spans : false,
        inline_styles: false,
        fix_table_elements : true,
        fix_list_elements : true,
        relative_urls : false,
        convert_urls : false,
        extended_valid_elements :
        "a[href|rel|rev|target|title|type]," +
    "b[],"+
    "blink[],"+
    "blockquote[align|cite|clear|height|type|width],"+
    "br[clear],"+
    "caption[align|height|valign|width],"+
    "center[align|height|width],"+
    "col[align|bgcolor|char|charoff|span|valign|width],"+
    "colgroup[align|bgcolor|char|charoff|span|valign|width],"+
    "comment[],"+
    "em[],"+
    "font[color|face|font-weight|point-size|size],"+
    "h1[align|clear|height|width],"+
    "h2[align|clear|height|width],"+
    "h3[align|clear|height|width],"+
    "h4[align|clear|height|width],"+
    "h5[align|clear|height|width],"+
    "h6[align|clear|height|width],"+
    "hr[align|clear|color|noshade|size|width],"+
    "img[align|alt|border|height|hspace|src|vspace|width],"+
    "li[align|clear|height|type|value|width],"+
    "marquee[behavior|bgcolor|direction|height|hspace|loop|scrollamount|scrolldelay|vspace|width],"+
    "ol[align|clear|height|start|type|width],"+
    "p[align|clear|height|width],"+
    "pre[clear|width|wrap],"+
    "s[],"+
    "small[],"+
    "span[align],"+
    "strike[],"+
    "strong[],"+
    "sub[],"+
    "sup[],"+
    "table[align|background|bgcolor|border|bordercolor|bordercolordark|bordercolorlight|"+
    "bottompadding|cellpadding|cellspacing|clear|cols|height|hspace|leftpadding|"+
    "rightpadding|rules|summary|toppadding|vspace|width],"+
    "tbody[align|bgcolor|char|charoff|valign],"+
    "td[abbr|align|axis|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|headers|"+
    "height|nowrap|rowspan|scope|valign|width],"+
    "tfoot[align|bgcolor|char|charoff|valign],"+
    "th[abbr|align|axis|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|headers|"+
    "height|nowrap|rowspan|scope|valign|width],"+
    "thead[align|bgcolor|char|charoff|valign],"+
    "tr[align|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|"+
    "height|nowrap|valign],"+
    "tt[],"+
    "u[],"+
    "ul[align|clear|height|start|type|width]"


    });
}

function showTranslationBox(id){

    $("#translation_"+id).html('<textarea id="translationArea_'+id+'" class="translationArea_'+id+'" cols="80" rows="8"></textarea>');


    tinyMCE.init({

        mode : "specific_textareas",
        editor_selector : "translationArea_"+id,

        language : "fr",
        height: "250",
        width: "900",
        plugins : "pagebreak,style,layer,table,save,advhr,inlinepopups,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
        theme : "advanced",
        theme_advanced_resizing : false,
        theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,|,\n\
justifyleft,justifycenter,justifyright,justifyfull,|,fontsizeselect,hr,removeformat,|,\n\
cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,|,\n\
undo,redo,|,link,unlink,cleanup,|,forecolor,backcolor",
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 :"",
        theme_advanced_buttons4 :"",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        cleanup: false,
        convert_fonts_to_spans : false,
        inline_styles: false,
        fix_table_elements : true,
        fix_list_elements : true,
        relative_urls : false,
        convert_urls : false,
        extended_valid_elements :
        "a[href|rel|rev|target|title|type]," +
    "b[],"+
    "blink[],"+
    "blockquote[align|cite|clear|height|type|width],"+
    "br[clear],"+
    "caption[align|height|valign|width],"+
    "center[align|height|width],"+
    "col[align|bgcolor|char|charoff|span|valign|width],"+
    "colgroup[align|bgcolor|char|charoff|span|valign|width],"+
    "comment[],"+
    "em[],"+
    "font[color|face|font-weight|point-size|size],"+
    "h1[align|clear|height|width],"+
    "h2[align|clear|height|width],"+
    "h3[align|clear|height|width],"+
    "h4[align|clear|height|width],"+
    "h5[align|clear|height|width],"+
    "h6[align|clear|height|width],"+
    "hr[align|clear|color|noshade|size|width],"+
    "img[align|alt|border|height|hspace|src|vspace|width],"+
    "li[align|clear|height|type|value|width],"+
    "marquee[behavior|bgcolor|direction|height|hspace|loop|scrollamount|scrolldelay|vspace|width],"+
    "ol[align|clear|height|start|type|width],"+
    "p[align|clear|height|width],"+
    "pre[clear|width|wrap],"+
    "s[],"+
    "small[],"+
    "span[align],"+
    "strike[],"+
    "strong[],"+
    "sub[],"+
    "sup[],"+
    "table[align|background|bgcolor|border|bordercolor|bordercolordark|bordercolorlight|"+
    "bottompadding|cellpadding|cellspacing|clear|cols|height|hspace|leftpadding|"+
    "rightpadding|rules|summary|toppadding|vspace|width],"+
    "tbody[align|bgcolor|char|charoff|valign],"+
    "td[abbr|align|axis|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|headers|"+
    "height|nowrap|rowspan|scope|valign|width],"+
    "tfoot[align|bgcolor|char|charoff|valign],"+
    "th[abbr|align|axis|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|headers|"+
    "height|nowrap|rowspan|scope|valign|width],"+
    "thead[align|bgcolor|char|charoff|valign],"+
    "tr[align|background|bgcolor|bordercolor|"+
    "bordercolordark|bordercolorlight|char|charoff|"+
    "height|nowrap|valign],"+
    "tt[],"+
    "u[],"+
    "ul[align|clear|height|start|type|width]"


    });
}

function savePrivateComment(id){
    $.post(privateCommentSaveURL, {
        ayatId: id,
        content:tinyMCE.get("privateCommentArea_"+id).getContent()
    },
    function(data){

        $("#message_"+id).html(commentAdd+"<br/>");
        $("#message_"+id).removeClass("validation");
        $("#message_"+id).addClass("success");
        $("#message_"+id).show();
        window.setTimeout(function() {
            $("#message_"+id).fadeOut('slow');
        }, 3000);
    });
}

function savePublicComment(id){
    $.post(publicCommentSaveURL, {
        ayatId: id,
        comment:tinyMCE.get("publicCommentArea_"+id).getContent()
    },
    function(data){
        if(data.result == 'ok'){
            $("#message_"+id).html(commentAddPublic+"<br/>");
            $("#message_"+id).removeClass("validation");
            $("#message_"+id).addClass("success");
            $("#message_"+id).show();
            window.setTimeout(function() {
                $("#message_"+id).fadeOut('slow');
            }, 3000);
        }else{
            $("#message_"+id).html(commentNotAddPublic+"<br/>");
            $("#message_"+id).removeClass("success");
            $("#message_"+id).addClass("validation");
            $("#message_"+id).show();
            window.setTimeout(function() {
                $("#message_"+id).fadeOut('slow');
            }, 5000);
        }
    });
}

function saveTranslation(id){
    $.post(translationSaveURL, {
        ayatId: id,
        translation:tinyMCE.get("translationArea_"+id).getContent()
    },
    function(data){
        if(data.result == 'ok'){
            $("#message_"+id).html(translationAdd+"<br/>");
            $("#message_"+id).removeClass("validation");
            $("#message_"+id).addClass("success");
            $("#message_"+id).show();
            window.setTimeout(function() {
                $("#message_"+id).fadeOut('slow');
            }, 3000);
        }else{
            $("#message_"+id).html(translationNotAdd+"<br/>");
            $("#message_"+id).removeClass("success");
            $("#message_"+id).addClass("validation");
            $("#message_"+id).show();
            window.setTimeout(function() {
                $("#message_"+id).fadeOut('slow');
            }, 5000);
        }
    });
}

function showHidePrivateCommentBox(id,load){
    if(username == ''){
        alert(userNotConnected);
        return;
    }

    if($('#privateCommentArea_'+id).length == 0 || load == "no"){
        showPrivateCommentBox(id,load);
        $("#privateCommentBox_"+id).toggle();
    }else{
        $("#privateCommentBox_"+id).toggle();
    }

}

function showHidePublicCommentBox(id){
    if(username == ''){
        alert(userNotConnected);
        return;
    }

    if($('#publicCommentArea_'+id).length == 0){
        showPublicCommentBox(id);
        $("#publicCommentBox_"+id).toggle();
    }else{
        $("#publicCommentBox_"+id).toggle();
    }

}

function showHideTranslateBox(id){
    if(username == ''){
        alert(userNotConnected);
        return;
    }

    if($('#translationArea_'+id).length == 0){
        showTranslationBox(id);
        $("#translationBox_"+id).toggle();
    }else{
        $("#translationBox_"+id).toggle();
    }

}

function selectAyat(id){
    if(username == ''){
        alert(userNotConnected);
        return;
    }

    $.post(selectAyatURL, {
        ayatId: id
    },
    function(data){
        if(data.result == 'ok'){
            $("#message_"+id).html(ayatSelected+"<br/>");
            $("#message_"+id).removeClass("validation");
            $("#message_"+id).addClass("success");
            $("#message_"+id).show();
            window.setTimeout(function() {
                $("#message_"+id).fadeOut('slow');
            }, 3000);
        }else{
            $("#message_"+id).html(ayatAlreadySelected+"<br/>");
            $("#message_"+id).removeClass("success");
            $("#message_"+id).addClass("validation");
            $("#message_"+id).show();
            window.setTimeout(function() {
                $("#message_"+id).fadeOut('slow');
            }, 3000);
        }
    });
}


function addAllSelectedAyatToChapter(){
    var chapterID = $("#privateChapter option:selected").val();
    var publicChapterID = $("#publicChapter option:selected").val();

    if(chapterID == "-1" && publicChapterID == "-1"){
        alert(noChapter);
        return;
    }
    
    $("input:checkbox:checked").each(function() {
        var ayatID = $(this).val();

        if(ayatID != "-1"){
            $("select[id='privateChapter_"+ayatID+"'] option[value='"+chapterID+"']").attr("selected","selected");
            $("select[id='publicChapter_"+ayatID+"'] option[value='"+publicChapterID+"']").attr("selected","selected");
            addToChapterSimple(ayatID);
        }

    });
}

function addToChapterSimple(ayatID){
    var chapterID = $("#privateChapter_"+ayatID+" option:selected").val();
    var publicChapterID = $("#publicChapter_"+ayatID+" option:selected").val();

    if(chapterID != "-1"){
        $.post(addAyatToChapterURL, {
            ayatID: ayatID,
            chapterID:chapterID
        },
        function(data){
            if(data.result == 'ok'){
                $("#allAyat_"+ayatID).hide();
            }else if(data.result == 'error'){
                $("#message_"+ayatID).html(noChapter+"<br/>");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
            }else{
                $("#message_"+ayatID).html(selectedAyatAlreadyAdded+"<br/>");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
            }
        });
    }
    if(publicChapterID != "-1"){
        $.post(addAyatToPublicChapterURL, {
            ayatID: ayatID,
            publicChapterID:publicChapterID
        },
        function(data){
            if(data.result == 'ok'){
                $("#allAyat_"+ayatID).hide();
            }else if(data.result == 'error'){
                $("#message2_"+ayatID).append(chapterError+"<br/>");
                $("#message2_"+ayatID).addClass("validation");
                $("#message2_"+ayatID).show();
            }else if(data.result == 'chapterAlreadyProposed'){
                $("#message2_"+ayatID).html(selectedAyatAlreadyProposed+"<br/>");
                $("#message2_"+ayatID).addClass("validation");
                $("#message2_"+ayatID).show();
            }else{
                $("#message2_"+ayatID).append(selectedAyatAlreadyAdded+"<br/>");
                $("#message2_"+ayatID).addClass("validation");
                $("#message2_"+ayatID).show();
            }
        });
    }
}

function addToChapter(ayatID){
    var chapterID = $("#privateChapter_"+ayatID+" option:selected").val();
    var publicChapterID = $("#publicChapter_"+ayatID+" option:selected").val();

    if(chapterID == "-1" && publicChapterID == "-1"){
        alert(noChapter);
        return;
    }

    if(chapterID != "-1"){
        $.post(addAyatToChapterURL, {
            ayatID: ayatID,
            chapterID:chapterID
        },
        function(data){
            if(data.result == 'ok'){
                $("#message_"+ayatID).html(selectedAyatAddedToChapter+"<br/>");
                $("#message_"+ayatID).addClass("success");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {

                    $("#allAyat_"+ayatID).fadeOut('slow');
                }, 3000);
            }else if(data.result == 'error'){
                $("#message_"+ayatID).html(noChapter+"<br/>");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message_"+ayatID).fadeOut('slow');

                }, 5000);
            }else{
                $("#message_"+ayatID).html(selectedAyatAlreadyAdded+"<br/>");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message_"+ayatID).fadeOut('slow');

                }, 5000);
            }
        });
    }
    if(publicChapterID != "-1"){
        $.post(addAyatToPublicChapterURL, {
            ayatID: ayatID,
            publicChapterID:publicChapterID
        },
        function(data){
            if(data.result == 'ok'){
                $("#message2_"+ayatID).append(selectedAyatProposed+"<br/>");
                $("#message2_"+ayatID).addClass("success");
                $("#message2_"+ayatID).show();
                window.setTimeout(function() {

                    $("#allAyat_"+ayatID).fadeOut('slow');
                }, 5000);
            }else if(data.result == 'chapterAlreadyProposed'){
                $("#message_"+ayatID).html(selectedAyatAlreadyProposed+"<br/>");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message_"+ayatID).fadeOut('slow');

                }, 5000);
            }else if(data.result == 'error'){
                $("#message2_"+ayatID).append(chapterError+"<br/>");
                $("#message2_"+ayatID).addClass("validation");
                $("#message2_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message2_"+ayatID).fadeOut('slow');

                }, 5000);
            }else{
                $("#message2_"+ayatID).append(selectedAyatAlreadyAdded+"<br/>");
                $("#message2_"+ayatID).addClass("validation");
                $("#message2_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message2_"+ayatID).fadeOut('slow');

                }, 5000);
            }
        });
    }
}

function sendTagProposition(ayatID){


    var data = $("#tagForm_"+ayatID).serialize();

    //start the ajax
    $.ajax({
        //this is the php file that processes the data and send mail
        url: newTagURL,

        //GET method is used
        type: "POST",

        //pass the data
        data: data,

        //Do not cache the page
        cache: false,

        //success
        success: function (data) {
            if (data.result == "ok") {
                $("#message_"+ayatID).html(tagProposed+"<br/>");
                $("#message_"+ayatID).removeClass("validation");
                $("#message_"+ayatID).addClass("success");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message_"+ayatID).fadeOut('slow');
                }, 3000);
            } else if(data.result == "existingTag"){
                $("#message_"+ayatID).html(existingTag+" : <strong>"+data.tags+"</strong><br/>");
                $("#message_"+ayatID).removeClass("success");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message_"+ayatID).fadeOut('slow');

                }, 5000);
            } else if(data.result == "noTag"){
                $("#message_"+ayatID).html(noTag+"<br/>");
                $("#message_"+ayatID).removeClass("success");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message_"+ayatID).fadeOut('slow');

                }, 5000);
            }else if(data.result == "noAyat"){
                $("#message_"+ayatID).html(noAyat+"<br/>");
                $("#message_"+ayatID).removeClass("success");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message_"+ayatID).fadeOut('slow');

                }, 5000);
            }
        }
    });

    //cancel the submit button default behaviours
    return false;
 
}

function removeTagAyat(tagName,ayatID){
    var result = confirm(confirmRemoveTagAyat);
    if(result){
        $.post(removeTagAyatURL, {
            ayatID: ayatID,
            tagName:tagName
        },
        function(data){
            if(data.result == 'ok'){
                $("#message_"+ayatID).html(ayatRemovedFromTag+"<br/>");
                $("#message_"+ayatID).removeClass("validation");
                $("#message_"+ayatID).addClass("success");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {

                    $("#allAyat_"+ayatID).fadeOut('slow');
                }, 3000);
            }else if(data.result == 'error'){
                $("#message_"+ayatID).html(noTag+"<br/>");
                $("#message_"+ayatID).removeClass("success");
                $("#message_"+ayatID).addClass("validation");
                $("#message_"+ayatID).show();
                window.setTimeout(function() {
                    $("#message_"+ayatID).fadeOut('slow');

                }, 5000);
            }
        });
    }
}

$.queueTimer = {
    _timer: null,
    _queue: [],
    add: function(fn, context, time) {
        var setTimer = function(time) {
            $.queueTimer._timer = setTimeout(function() {
                time = $.queueTimer.add();
                if ($.queueTimer._queue.length) {
                    setTimer(time);
                }
            }, time || 2);
        }

        if (fn) {
            $.queueTimer._queue.push([fn, context, time]);
            if ($.queueTimer._queue.length == 1) {
                setTimer(time);
            }
            return;
        }

        var next = $.queueTimer._queue.shift();
        if (!next) {
            return 0;
        }
        next[0].call(next[1] || window);
        return next[2];
    },
    clear: function() {
        clearTimeout($.queueTimer._timer);
        $.queueTimer._queue = [];
    }
};