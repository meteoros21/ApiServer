/**
 * 
 */

CKEDITOR.plugins.add('mycommands', {
	requires:['richcombo'],
	init:function(editor) {
		
		var strings = [];
		strings.push(['formTable', '폼테이블', '폼테이블']);
		strings.push(['text', '텍스트 콘트롤', '텍스트 콘트롤']);
		strings.push(['checkbox', '체크박스', '체크박스']);
		strings.push(['radio', '라디오 버튼', '라디오 버튼']);
		strings.push(['calendar', '달력 콘트롤', '달력 콘트롤']);
		strings.push(['file', '파일첨부 콘트롤', '파일첨부 콘트롤']);
		
		editor.ui.addRichCombo('mycommands', {
			label:'콘텐츠',
			title:'Insert Content',
			voiceLabel:'Insert Content',
			className:'cke_format',
			multiSelect:false,
			panel: {
				css: [ editor.config.contentsCss, CKEDITOR.skin.getPath('editor') ],
				voiceLabel: editor.lang.panelVoiceLabel
			},
			
			init:function() {
				this.startGroup('Insert Content');
				for (var i in strings)
				{
					this.add(strings[i][0], strings[i][1], strings[i][2]);
				}
			},
			
			onClick:function(value)
			{
				if (value == 'formTable')
				{
					showTableModal();
				}
				else if (value == 'text' || value == 'calendar' || value == 'file')
				{
					showControlModal(value);
				}
				else if (value == 'checkbox' || value == 'radio')
				{
					showButtonModal(value);
				}
//				editor.focus();
//				editor.fire('saveSnapshot');
//				editor.insertHtml(value);
//				editor.fire('saveSnapshot')
			}
		});
		
//		if (editor.contextMenu)
//		{
//			editor.addMenuGroup('mycommandsGroup');
//			editor.addMenuItem('textItem', {
//				label: '텍스트 콘트롤 수정',
//				icon: '',
//				command: 'mycommands',
//				group: 'mycommandsGroup'
//			});
//			editor.addMenuItem('calendarItem', {
//				label: '달력 콘트롤 수정',
//				command: 'mycommands',
//				group: 'mycommandsGroup'
//			});
//			
//			editor.contextMenu.addListener(function (element) {
//				if (element.getAscendant('input', true))
//				{
//					return {textItem: CKEDITOR.TRISTATE_OFF};
//				}
//			});
//		}
	}
});