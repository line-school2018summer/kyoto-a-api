package com.example.apiSample.Requests

import io.swagger.annotations.ApiModelProperty

class PostMessageRequest (
        @ApiModelProperty(example="メッセージ", position=0)
        var text: String = ""
)
