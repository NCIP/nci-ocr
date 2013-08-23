CKEDITOR.editorConfig = function( config )
{
  config.toolbar = 'FirebirdDefault';

  config.toolbar_FirebirdDefault =
      [
       { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
       { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','SpellChecker'] },
       { name: 'paragraph', items : [ 'Outdent','Indent','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'] },
       { name: 'tools', items : [ 'Maximize'] },
       { name: 'links', items : [ 'Link','Unlink','Anchor' ] },
       { name: 'insert', items : [ 'HorizontalRule'] },
       '/',
       { name: 'basicstyles', items : [ 'NumberedList','BulletedList','-','Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
       { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] }
   ];

};