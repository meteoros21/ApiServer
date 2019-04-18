/**
 * 
 */

CKEDITOR.plugins.add('myimage', {
	icons:'myimage',
	init:function(editor) {
		
		editor.addCommand('myimage', new CKEDITOR.dialogCommand('myimageDialog'));
		editor.ui.addButton('myimage', {
			label:editor.lang.common.image,
			command:'myimage',
			icon:this.path + 'icons/myimage2.png',
			toolbar:'insert'
		});
		CKEDITOR.dialog.add('myimageDialog', function(editor) {
			
			return {
				title: 'Image',
				minWidth: 400,
				minHeight: 200,
				contents:
				[
					{
						id : 'general',
						label : 'Settings',
						elements :
						[
							{
								type: 'html',
								html: '<h1 style="font-size:14px">본문에 삽입할 이미지와 이미지의 속성값을 입력해 주세요.</h1>'
							},
							{
								type: 'file',
								id: 'myimageFile',
								label: '이미지 파일',
								commit: function(data) {
									data.myimageFileId = this.getValue();
									data.myimageFile = this.getInputElement();
								}
							},
//							{
//								type: 'fileButton',
//								id: 'fileId',
//								label: 'Upload file',
//								'for': ['myimageFile'],
//								filebrowser: {
//									onSelect: function(fileUrl, data) {
//										alert('hello ' + fileUrl);
//									}
//								}
//							},
							{
								type: 'text',
								id: 'myimageClass',
								label: 'class',
								commit: function(data) {
									data.myimageClass = this.getValue();
								}
							},
							{
								type: 'text',
								id: 'myimageWidth',
								label: '넓이(픽셀)',
								commit: function(data) {
									data.myimageWidth = this.getValue();
								}
							},
							{
								type: 'text',
								id: 'myimageHeight',
								label: '높이(픽셀)',
								commit: function(data) {
									data.myimageHeight = this.getValue();
								}
							}
						]
					}
				],
				onShow: function() {
					CKEDITOR.dialog.getCurrent().hide();
					showUploadModal();
				},
				onOk : function() {
					var dialog = this;
					var data = {};
					this.commitContent(data);

					//var test2 = $('input[type=file][name=myimageFile');
					var test = document.getElementsByName('myimageFile');
					alert(test.value);
					var img = editor.document.createElement('img');
					
					img.setAttribute('src', 'http://localhost:8080/kpga/prod_img/prod/12/test-thumb[2].jpg');
					
					editor.insertElement(img);
				}
			};
		});
	}
});