const app = Vue.createApp({
    data () {
        return {
            apiURL: "http://localhost/api/v1/optimize",
            file: null,
            data: {},
            drag: false
        }
    },
    methods: {
        handleDrag (e) {
            e.preventDefault ()
            this.drag = true;
        },
        dragStopped () {
            console.log ("Stoped")
            this.drag = false
        },
        handleFileChange () {
            this.file = this.$refs.file.files[0]
        },
        handleDrop (e) {
            e.preventDefault ()
            if (e.dataTransfer.files.length != 1) {
                alert ("Please enter only 1 text file")
                return
            }
            this.$refs.file.files = e.dataTransfer.files
            this.handleFileChange ()
            this.drag = false
        },
        getOptimized () {
            if (!this.file) {
                alert ("Please select a file")
                return
            }
            const formData = new FormData ();
            formData.append("file", this.file)
            fetch (this.apiUrl, {
                method: 'POST',
                body: formData
            })
            .then (response => response.json())
            .then (data => {
                if (data?.error) {                    
                    throw new Error (data.error)
                }
                this.data = data
            })
            .catch (error => {
                this.file = null
                alert (error)
                console.error ("Error: ", error)
            })
        },
        getOptimizedTest () {
            fetch (this.apiUrl, {
                method: 'GET',
            })
            .then (response => response.json())
            .then (data => {
                if (data?.error) {                    
                    throw new Error (data.error)
                }
                this.data = data
            })
            .catch (error => {
                this.file = null
                alert (error)
                console.error ("Error:", error)
            })
        }
    }
})
app.mount ('#app')