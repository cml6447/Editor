$(function () {
    var monacoEditor;
    require.config(
        {paths: {'vs': 'package/min/vs'}}
    );
    require(['vs/editor/editor.main'],function(){
    	monacoEditor = monaco.editor.create(document.getElementById('container'),{
    		 value: [
    		        'public class demo{',
                    '\tpublic static void main(String args[]){',
                    '\t\t//body',
                    '\t}',
		            '}'].join("\n"),
    	   language: 'java',//编译语言为java
    	   wrappingColumn: 0,//缩进为0
		  	//wordwrap:"on",
    	 	wrappingIndent: "indent"//自动换行
    	});

        //黑色主题
    	monaco.editor.setTheme("vs-dark");
      monacoEditor.getValue();

    });
});
    //重置代码
    function ReCreateEditor() {
    
        $("#container").children().remove();//把以前的编辑器先清空
        require(['vs/editor/editor.main'],function(){
    
            monacoEditor = monaco.editor.create(document.getElementById('container'),{
                value: [
                    'public class demo{' ,
                    '\tpublic static void main(String args[]){',
                    '\t\t//body',
                    '\t}',
                    '}'].join("\n"),
                language: 'java',
                background: "black",
                wrappingColumn: 20,
               // wordwrap:"on",
                wrappingIndent: "indent"
            });
        });
    }

    function postmsg() {       
        $.ajax({
                    type:"post",
                    url:"/editor/demo",
                    dataType:'json',
                    data:{code:$(".inputarea").text(),
                           name: "demo"},
                    success:function (json) {
                        $(".p2").html(json.message);

                    },
                    error:function () {
                        alert("网络错误");
                    }
            }
        );
    
    }
