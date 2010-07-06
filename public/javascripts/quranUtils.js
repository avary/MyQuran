function showPrivateCommentBox(id){
    $("#privateComment_"+id).html('<textarea id="privateCommentArea_'+id+'" class="privateCommentArea_'+id+'" cols="80" rows="8"></textarea>');

    $.post(privateCommentLoadURL, {
        ayatId: id
    },
    function(data){
        $("#privateCommentArea_"+id).html(data.content);
    });


    tinyMCE.init({

        mode : "specific_textareas",
        editor_selector : "privateCommentArea_"+id,

        plugins : "save",
        theme : "advanced",
        theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,forecolor,|,justifyleft,justifycenter,justifyright,justifyfull",
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 :"",
        theme_advanced_buttons4 :"",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "bottom",
        theme_advanced_resizing : true,
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

        plugins : "save",
        theme : "advanced",
        theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,forecolor,|,justifyleft,justifycenter,justifyright,justifyfull",
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 :"",
        theme_advanced_buttons4 :"",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "bottom",
        theme_advanced_resizing : true,
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

        plugins : "save",
        theme : "advanced",
        theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,forecolor,|,justifyleft,justifycenter,justifyright,justifyfull",
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 :"",
        theme_advanced_buttons4 :"",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "bottom",
        theme_advanced_resizing : true,
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

function showHidePrivateCommentBox(id){
    if(username == ''){
        alert(userNotConnected);
        return;
    }

    if($('#privateCommentArea_'+id).length == 0){
        showPrivateCommentBox(id);
        $("#privateComment_"+id).toggle();
    }else{
        $("#privateComment_"+id).toggle();
    }

}

function showHidePublicCommentBox(id){
    if(username == ''){
        alert(userNotConnected);
        return;
    }

    if($('#publicCommentArea_'+id).length == 0){
        showPublicCommentBox(id);
        $("#publicComment_"+id).toggle();
    }else{
        $("#publicComment_"+id).toggle();
    }

}

function showHideTranslateBox(id){
    if(username == ''){
        alert(userNotConnected);
        return;
    }

    if($('#translationArea_'+id).length == 0){
        showTranslationBox(id);
        $("#translation_"+id).toggle();
    }else{
        $("#translation_"+id).toggle();
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
