---
name: xhs-note-creator
description: Create and publish Xiaohongshu (小红书) notes with AI assistance. Use when you need to write XHS content, format posts with proper styling, add hashtags, create engaging titles, or publish notes to Xiaohongshu. Supports both text-image posts and video content.
---

# XHS Note Creator

A skill for creating engaging Xiaohongshu (小红书) content.

## Overview

Xiaohongshu (小红书) is a popular Chinese lifestyle sharing platform. This skill helps you:
- 📝 Write engaging XHS-style content
- 🏷️ Add trending hashtags
- 🎨 Format posts for maximum engagement
- 📊 Analyze content performance
- 🚀 Publish notes (with xhs-toolkit integration)

## XHS Content Best Practices

### 1. Title Formula

**Good XHS titles:**
- Use numbers: "5个方法..." "3天学会..."
- Create curiosity: "终于知道为什么..." "原来..."
- Add emotion: "太绝了！" "救命！" "谁懂啊！"
- Include benefits: "省钱攻略" "避坑指南"

**Examples:**
- ❌ "我的旅行日记"
- ✅ "人均500！云南小众旅行地，美到窒息😭"

### 2. Content Structure

```
[Hook - 前3行抓住注意力]
[Problem/Pain Point - 引起共鸣]
[Solution - 提供价值]
[Details - 具体步骤/建议]
[CTA - 互动引导]
[Hashtags - 精准标签]
```

### 3. Writing Style

**XHS Voice:**
- Casual and friendly (like talking to a friend)
- Use emojis liberally ✨🌟💫
- Short paragraphs (2-3 lines max)
- Personal experience sharing
- Authentic and relatable

**Common phrases:**
- "姐妹们！" / "家人们！"
- "谁懂啊！"
- "太绝了！"
- "救命！"
- "挖到宝了"
- "亲测有效"
- "抄作业"

### 4. Hashtag Strategy

**Use 5-10 hashtags:**
- 2-3 broad tags (high volume): #生活记录 #日常分享
- 3-5 niche tags (targeted): #极简生活 #职场穿搭
- 1-2 branded/tags: #小红书成长笔记

## Content Templates

### Template 1: Product Review
```
标题：挖到宝了！这个[产品]真的绝了✨

姐妹们！最近发现了一个超棒的[产品]！

[痛点描述]
之前一直[遇到的问题]，试了好多方法都不行😭

[解决方案]
直到发现了这个！用了[时间]真的惊艳到我了

[具体优点]
✅ [优点1]
✅ [优点2]  
✅ [优点3]

[使用体验]
[详细描述使用感受]

[价格/购买信息]
💰 [价格]
📍 [购买渠道]

[CTA]
姐妹们冲！真的不踩雷！

#好物分享 #[产品类别] #生活好物 #种草 #推荐
```

### Template 2: Tutorial/Guide
```
标题：3步学会[技能]！手残党也能轻松搞定✨

[Hook]
谁懂啊！原来[技能]这么简单！

[Problem]
之前一直觉得[技能]很难，[遇到的问题]

[Solution Overview]
今天分享一个超简单的方法，3步就能搞定！

Step 1️⃣: [第一步]
[详细说明]

Step 2️⃣: [第二步]
[详细说明]

Step 3️⃣: [第三步]
[详细说明]

[Results]
效果真的绝了！看对比图👆

[Tips]
💡 小贴士：[额外建议]

[CTA]
快收藏起来试试吧！有问题评论区问我～

#教程 #[技能标签] #干货分享 #新手教程
```

### Template 3: Lifestyle/Daily
```
标题：[地点/场景] | [情感/体验]的一天✨

[Opening]
[时间]的[地点]真的太[形容词]了！

[Experience]
[详细描述经历/感受]

[Highlights]
🌟 [亮点1]
🌟 [亮点2]
🌟 [亮点3]

[Photos]
[图片描述]

[Reflection]
[感悟/总结]

[CTA]
你们有类似的体验吗？评论区聊聊～

#生活记录 #[地点标签] #日常 #[情感标签]
```

## Image Guidelines

### Image Specs
- **Ratio**: 3:4 or 1:1 (portrait preferred)
- **Resolution**: 1080x1440px (3:4) or 1080x1080px (1:1)
- **Format**: JPG or PNG
- **Quality**: High resolution, clear and bright

### Image Tips
- First image is most important (thumbnail)
- Use bright, warm colors
- Show real scenarios (not stock photos)
- Add text overlays for key points
- Consistent aesthetic/style

## Publishing Workflow

### Step 1: Content Creation
1. Choose topic and template
2. Write draft using XHS style
3. Select/create images
4. Add hashtags

### Step 2: Optimization
1. Review title (is it catchy?)
2. Check first 3 lines (hook test)
3. Verify hashtags (relevant and trending?)
4. Add emojis appropriately

### Step 3: Publishing
```bash
# Using xhs-toolkit (if configured)
xhs publish --title "..." --content "..." --images "..."
```

## Content Categories

### Popular XHS Niches
- 🛍️ Shopping/Product reviews
- 💄 Beauty/Makeup tutorials
- 🍜 Food/Recipes
- ✈️ Travel guides
- 💼 Career/Productivity
- 🏠 Home/Decor
- 👗 Fashion/Outfits
- 📚 Learning/Education
- 💪 Fitness/Health
- 💰 Personal finance

## Engagement Tips

### Increase Likes/Comments
- Ask questions in content
- Respond to all comments quickly
- Create "save-worthy" content (reference material)
- Post consistently (1-3 times/day)
- Engage with similar accounts

### Best Posting Times
- Weekday mornings: 7-9 AM
- Lunch break: 12-1 PM
- Evening: 6-10 PM
- Weekend: 10 AM - 8 PM

## Tools Integration

### With xhs-toolkit
```bash
# Install xhs-toolkit
pip install xhs-toolkit

# Login
xhs login

# Publish
xhs publish --file note.md --images ./images/
```

### With Agent Reach
```bash
# Search trending topics
agent-reach xiaohongshu search "关键词"

# Analyze competitor content
agent-reach xiaohongshu get-feed-detail "笔记ID"
```

## Analytics & Improvement

### Key Metrics
- 👁️ Views (浏览量)
- ❤️ Likes (点赞)
- 💬 Comments (评论)
- ⭐ Saves (收藏)
- 👥 Shares (分享)

### Content Audit
- Which posts performed best?
- What time got most engagement?
- Which hashtags worked?
- What topics resonated?

## Example Notes

### Example 1: Product Review
```
标题：9命！这个面霜真的太好用了😭

姐妹们！挖到宝了！

之前皮肤状态超差，干燥起皮还敏感
试了好多护肤品都没用😭

直到闺蜜推荐了这个面霜！
用了两周真的惊艳到我了！

✅ 保湿效果绝了
✅ 质地超清爽不黏腻
✅ 敏感肌也能用

现在皮肤状态稳定多了
化妆都不卡粉了！

💰 200+ 学生党也能冲！

#面霜推荐 #护肤分享 #敏感肌 #好物推荐
```

### Example 2: Travel Guide
```
标题：人均500！云南这个宝藏小城美到窒息✨

姐妹们！发现了一个超小众的旅行地！

大理丽江人太多了
这个地方真的又美人又少！

🌟 必打卡：
1️⃣ [景点1] - 看日出绝了
2️⃣ [景点2] - 拍照超出片
3️⃣ [景点3] - 当地人才知道

🍜 必吃美食：
- [美食1]
- [美食2]

💰 3天2晚人均500！
🏨 住宿推荐：[酒店名]

快收藏起来！下次旅行安排上！

#云南旅行 #小众旅行地 #旅行攻略 #穷游
```

## Related Skills

- **humanizer** - Make content sound more natural
- **copywriting** - Write compelling copy
- **canvas-design** - Create images for XHS posts
- **xhs-toolkit** - Publish to Xiaohongshu platform

## Resources

- [Xiaohongshu Creator Center](https://creator.xiaohongshu.com)
- [XHS Content Guidelines](https://www.xiaohongshu.com)
- Trending hashtags: Check XHS Discover page
